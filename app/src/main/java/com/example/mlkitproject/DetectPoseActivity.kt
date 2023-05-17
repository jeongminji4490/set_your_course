/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mlkitproject

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mlkitproject.camera.CameraSource
import com.example.mlkitproject.camera.CameraSourcePreview
import com.example.mlkitproject.posedetector.PoseDetectorProcessor
import com.example.mlkitproject.preference.PreferenceUtils
import com.example.mlkitproject.preference.SettingsActivity
import com.example.mlkitproject.viewmodel.CountViewModel
import com.google.android.gms.common.annotation.KeepName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/** Live preview demo for ML Kit APIs.  */
@KeepName
class DetectPoseActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private val dataStore = App.getInstance().getDataStore()
    private var exerciseType : String ?= null
    private var targetCount : String ?= null
    private val viewModel: CountViewModel by viewModels()
    private val dataStoreScope by lazy { CoroutineScope(Dispatchers.Main) }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pose_detect)

        onClickRequestPermission()

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        val settingsButton = findViewById<ImageView>(R.id.settings_button)
        val exerciseText = findViewById<TextView>(R.id.tx_exercise_type)
        val countText = findViewById<TextView>(R.id.tx_count)
        val exerciseImg = findViewById<ImageView>(R.id.img_exercise)

        preview = findViewById(R.id.preview_view)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        dataStoreScope.launch {
            // exerciseType = dataStore.exerciseType.first()
            targetCount = dataStore.squatCount.first()
            exerciseText.text = exerciseType
            if (exerciseType == "Squat") {
                Glide
                    .with(this@DetectPoseActivity)
                    .load(R.drawable.squat)
                    .into(exerciseImg)
                viewModel.getSquatsCountValue()
            } else {
                Glide
                    .with(this@DetectPoseActivity)
                    .load(R.drawable.pushup)
                    .into(exerciseImg)
                viewModel.getPushUpCountValue()
            }
        }

        viewModel.squatsCount.observe(this, Observer {
            if (it == targetCount) {
                Toast.makeText(this, "Target!", Toast.LENGTH_SHORT).show()
            } else {
                countText.text = it
            }
        })

        viewModel.pushupCount.observe(this, Observer {
            if (it == targetCount) {

            } else {
                countText.text = it
            }
        })

        facingSwitch.setOnCheckedChangeListener(this)

        settingsButton.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(
                SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW
            )
            startActivity(intent)
        }
        createCameraSource(POSE_DETECTION)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Log.d(TAG, "Set facing")
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource!!.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource!!.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        preview!!.stop()
        startCameraSource()
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }
        try {
            when (model) {
                POSE_DETECTION -> {
                    val poseDetectorOptions =
                        PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
                    Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
                    val shouldShowInFrameLikelihood =
                        PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
                    val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
                    val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
                    val runClassification =
                        PreferenceUtils.shouldPoseDetectionRunClassification(this)
                    cameraSource!!.setMachineLearningFrameProcessor(
                        PoseDetectorProcessor(
                            this,
                            poseDetectorOptions,
                            shouldShowInFrameLikelihood,
                            visualizeZ,
                            rescaleZ,
                            runClassification,  /* isStreamMode = */
                            true
                        )
                    )
                }

                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: RuntimeException) {
            Log.e(TAG, "Can not create image processor: $model", e)
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    // Request permission for using camera
    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {  }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }

            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )
            }
        }
    }


    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createCameraSource(POSE_DETECTION)
        startCameraSource()
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        preview!!.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource!!.release()
        }
    }

    companion object {
        private const val POSE_DETECTION = "Pose Detection"
        private const val TAG = "PoseDetectActivity"
    }
}