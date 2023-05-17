package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartRoundActivity : AppCompatActivity() {

    private val dataStore = App.getInstance().getDataStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start_round)

        val startText = findViewById<TextView>(R.id.tx_start)
        val countText = findViewById<TextView>(R.id.tx_round_count)

        CoroutineScope(Dispatchers.Main).launch {
            val currentRound = dataStore.currentRound.first()
            countText.text = currentRound

            val nextRound = (currentRound.toInt() + 1).toString()
            dataStore.setCurrentRound(nextRound)

            delay(2000)
            startText.visibility = TextView.VISIBLE

            delay(3000)
            val intent = Intent(this@StartRoundActivity, DetectPoseTestActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}