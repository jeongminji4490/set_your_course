package com.example.mlkitproject.camera

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.mlkitproject.GraphicOverlay
import com.example.mlkitproject.SquatDetectFragment
import com.example.mlkitproject.posedetector.PoseDetectorProcessor
import com.example.mlkitproject.preference.PreferenceUtils
import java.io.IOException

class CameraUtils(val context: Context, val graphicOverlay: GraphicOverlay) {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
//    private var graphicOverlay: GraphicOverlay? = null

//    private var context: Context
//
//    init {
//        this.context = context
//    }

    fun createCameraSource() {
        // If there's no existing cameraSource, create one.
        val activity = context as Activity

        if (cameraSource == null) {
            cameraSource = CameraSource(activity, graphicOverlay)
        }
        try {
            val poseDetectorOptions =
                PreferenceUtils.getPoseDetectorOptionsForLivePreview(context)
            Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
            val shouldShowInFrameLikelihood =
                PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(context)
            val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(context)
            val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(context)
            val runClassification =
                PreferenceUtils.shouldPoseDetectionRunClassification(context)
            cameraSource!!.setMachineLearningFrameProcessor(
                PoseDetectorProcessor(
                    context,
                    poseDetectorOptions,
                    shouldShowInFrameLikelihood,
                    visualizeZ,
                    rescaleZ,
                    runClassification,  /* isStreamMode = */
                    true
                )
            )
        } catch (e: RuntimeException) {
            Toast.makeText(
                activity.applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    fun startCameraSource() {
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

    companion object {
        const val TAG = "CameraUtils"
    }
}