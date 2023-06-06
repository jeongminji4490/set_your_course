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
import com.example.mlkitproject.mlkit.preference.SettingsActivity

/**
 * Host Activity for both fragments (SquatDetectFragment, PushUpDetectFragment)
 * the exercise sequence is from squat to pushup
 * so the first fragment is always SquatFragment, and the PushUpFragment is always second
 */

class PoseActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

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
            val intent = Intent(this@PoseActivity, MainActivity::class.java)
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

    // Set fragment should be displayed
    private fun setFragment(fragmentType: String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (fragmentType == "push_up") {
            val pushUpDetectFragment = PushUpFragment()
            fragmentTransaction.replace(R.id.frame_layout, pushUpDetectFragment)
            fragmentTransaction.commit()
        } else {
            val squatDetectFragment = SquatFragment()
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

/**
 * Note
 * this activity determines which fragment should be displayed as the intent value
 * as I wrote in the above comment, the exercise sequence is always squat to pushup
 * actually there is no need to use intent value, because the sequence would always be the same
 * so it could just move from SquatFragment to PushupFragment without using an intent value
 * but when the user has completed the 1 course, DialogFragment is displayed, and The dialog button determines whether move to PushUpFragment or RoundActivity
 * If I implement it to move to the next fragment directly without moving to the host activity in DialogFragment, the source code can be complicated and readability can be reduced
 * Therefore, it is more efficient to move from DialogFragment to the host activity, and to determine which child fragment to move to by the intent value
 */