package com.example.mlkitproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mlkitproject.preference.SettingsActivity

class DetectPoseActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@DetectPoseActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pose_detect)

        onClickRequestPermission()

        val intentFromBeforeActivity = intent
        val fragmentType = intentFromBeforeActivity.getStringExtra("fragment_type")

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

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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