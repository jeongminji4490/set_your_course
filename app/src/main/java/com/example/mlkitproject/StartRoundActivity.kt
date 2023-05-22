package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mlkitproject.viewmodel.CountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StartRoundActivity : AppCompatActivity() {

    private val dataStore = App.getInstance().getDataStore()
    private val viewModel: CountViewModel by viewModels()
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

        setContentView(R.layout.activity_start_round)

        val startText = findViewById<TextView>(R.id.tx_start)
        val countText = findViewById<TextView>(R.id.tx_round_count)
        countText.text = currentRound

        lifecycleScope.launch {

            val setTextVisibleJob = launch {
                delay(2000)
                startText.visibility = TextView.VISIBLE
            }

            setTextVisibleJob.join()

            val intent = Intent(this@StartRoundActivity, DetectPoseActivity::class.java)
            intent.putExtra("fragment_type", "squat")
            startActivity(intent)
            finish()
        }

        currentRound = viewModel.currentRound
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }
}