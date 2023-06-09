package com.example.mlkitproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.mlkitproject.mlkit.CameraSource
import com.example.mlkitproject.mlkit.CameraSourcePreview
import com.example.mlkitproject.mlkit.GraphicOverlay
import com.example.mlkitproject.mlkit.posedetector.PoseDetectorProcessor
import com.example.mlkitproject.mlkit.preference.PreferenceUtils
import com.example.mlkitproject.viewmodel.CountViewModel
import java.io.IOException

/**
 * @file SquatFragment.kt
 * @author jeongminji4490
 * @brief This is the SquatFragment that detect user's squat pose and calculate count if user has the correct pose
 */
class SquatFragment : Fragment() {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private val viewModel: CountViewModel by viewModels()
    private var requiredCount: Int = -1 // initial count set by user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pose_detect_squat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countText = view.findViewById<TextView>(R.id.tx_count)
        val exerciseImg = view.findViewById<ImageView>(R.id.img_exercise)

        Glide
            .with(this)
            .load(R.drawable.squat)
            .into(exerciseImg)

        preview = view.findViewById(R.id.preview_view)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }

        graphicOverlay = view.findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }else {
            createCameraSource()
        }

        viewModel.currentSquatsCount.observe(viewLifecycleOwner) { currentCount ->

            if (currentCount == requiredCount) { // if user has completed exercise

                val dialog = DialogFragment()
                val bundle = Bundle()
                bundle.putString("button_type", "next") // to set button type of dialog fragment
                dialog.arguments = bundle

                if (!dialog.isAdded) {
                    dialog.show(requireActivity().supportFragmentManager.beginTransaction(), "workout complete dialog")
                }

            } else {
                countText.text = currentCount.toString()
            }

        }

        requiredCount = viewModel.targetSquatCount

    }

    private fun createCameraSource() {
        // If there's no existing cameraSource, create one.
        val context = requireContext()
        if (cameraSource == null) {
            cameraSource = CameraSource(
                requireActivity(),
                graphicOverlay
            )
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
                    true,
                    "squats_down"
                )
            )
        } catch (e: RuntimeException) {
            Toast.makeText(
                activity?.applicationContext,
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

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        createCameraSource()
        startCameraSource()
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
        preview!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource!!.release()
        }
        Log.e(TAG, "onDestroy")
    }

    companion object {
        const val TAG = "SquatDetectFragment"
    }
}