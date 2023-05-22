package com.example.mlkitproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mlkitproject.camera.CameraSource
import com.example.mlkitproject.camera.CameraSourcePreview
import com.example.mlkitproject.posedetector.PoseDetectorProcessor
import com.example.mlkitproject.preference.PreferenceUtils
import com.example.mlkitproject.preference.SettingsActivity
import java.io.IOException

class DetectPoseTestActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

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
        setContentView(R.layout.activity_pose_detect_test)

        onClickRequestPermission()

        val intent = intent
        val fragmentType = intent.getStringExtra("fragment_type")

        if (fragmentType != null)
            setFragment(fragmentType)

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        val settingsButton = findViewById<ImageView>(R.id.settings_button)

        facingSwitch.setOnCheckedChangeListener(this)

        settingsButton.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(
                SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW
            )
            startActivity(intent)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Log.d(TAG, "Set facing")
    }

    private fun setFragment(fragmentType: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentType == "push_up") {
            val pushUpDetectFragment = PushUpDetectFragment()
            fragmentTransaction.replace(R.id.frame_layout, pushUpDetectFragment)
            fragmentTransaction.commit()
        } else {
            val squatDetectFragment = SquatDetectFragment()
            fragmentTransaction.replace(R.id.frame_layout, squatDetectFragment)
            fragmentTransaction.commit()
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

    companion object {
        private const val TAG = "PoseDetectTestActivity"
    }
}