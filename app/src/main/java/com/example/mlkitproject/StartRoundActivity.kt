package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mlkitproject.databinding.ActivityStartRoundBinding
import com.example.mlkitproject.viewmodel.RoundViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartRoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartRoundBinding

    private lateinit var viewModel: RoundViewModel
    private var currentRound: String = ""

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@StartRoundActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartRoundBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[RoundViewModel::class.java]

        lifecycleScope.launch {
            setTextVisible()
        }

        currentRound = viewModel.currentRound
        binding.txRoundCount.text = currentRound
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private suspend fun setTextVisible() {
        val setTextVisibleJob = lifecycleScope.launch {
            delay(2000)
            binding.txStart.visibility = TextView.VISIBLE
        }

        setTextVisibleJob.join()

        goToDetectPoseActivity()
    }

    private fun goToDetectPoseActivity() {
        val intent = Intent(this@StartRoundActivity, DetectPoseActivity::class.java)
        intent.putExtra("fragment_type", "squat")
        startActivity(intent)
        finish()
    }
}