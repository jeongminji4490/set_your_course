package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mlkitproject.databinding.ActivityStartRoundBinding
import com.example.mlkitproject.viewmodel.RoundViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @file RoundActivity.kt
 * @author jeongminji4490
 * @brief This is the RoundActivity that display current round
 */
class RoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartRoundBinding

    private val viewModel: RoundViewModel by viewModels()
    private var currentRound: String = ""

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@RoundActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartRoundBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launch {
            setTextVisible()
        }

        currentRound = viewModel.currentRound
        binding.txRoundCount.text = currentRound
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    /**
     * @Note
     * ui event : the specific text is displayed after 2 seconds
     * and after the job is completed, move to the next screen
     * I use coroutine to handle this event
     */
    private suspend fun setTextVisible() {
        val setTextVisibleJob = lifecycleScope.launch {
            delay(2000)
            binding.txStart.visibility = TextView.VISIBLE
        }

        setTextVisibleJob.join()

        goToDetectPoseActivity()
    }

    private fun goToDetectPoseActivity() {
        val intent = Intent(this@RoundActivity, PoseActivity::class.java)
        intent.putExtra("fragment_type", "squat")
        startActivity(intent)
        finish()
    }

}