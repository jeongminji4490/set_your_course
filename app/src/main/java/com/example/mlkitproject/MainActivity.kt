package com.example.mlkitproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mlkitproject.databinding.ActivityMainBinding
import com.example.mlkitproject.viewmodel.CountViewModel
import com.example.mlkitproject.viewmodel.RoundViewModel
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var splashScreen: androidx.core.splashscreen.SplashScreen
    private val roundViewModel: RoundViewModel by viewModels()
    private val countViewModel: CountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnUpCount.setOnClickListener(this)
        binding.btnDownCount.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_up_count -> {
                upCount()
            }
            R.id.btn_down_count -> {
                downCount()
            }
            R.id.btn_start -> {
                try {
                    val squatCount = binding.edtSquatCount.text.toString().toInt()
                    val pushUpCount = binding.edtPushUpCount.text.toString().toInt()
                    val setRound = binding.txSetCount.text.toString()
                    setInitCourse(squatCount, pushUpCount, setRound)
                } catch (e: NumberFormatException) {
                    Log.e(TAG, e.toString())
                    Toast.makeText(this, "Numbers only", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun upCount() {
        var num = binding.txSetCount.text.toString().toInt()
        num += 1
        if (num > 50) {
            Toast.makeText(this, "Exceeded maximum number", Toast.LENGTH_SHORT).show()
        } else {
            binding.txSetCount.text = num.toString()
        }
    }

    private fun downCount() {
        var num = binding.txSetCount.text.toString().toInt()
        num -= 1
        if (num < 1) {
            Toast.makeText(this, "0 times not allowed", Toast.LENGTH_SHORT).show()
        } else {
            binding.txSetCount.text = num.toString()
        }
    }

    private fun setInitCourse(
        squatCount: Int,
        pushUpCount: Int,
        round: String
    ) {
        if (squatCount == 0 || pushUpCount == 0) {
            Toast.makeText(this@MainActivity, "Please enter the count", Toast.LENGTH_SHORT).show()
        } else {
            with(roundViewModel) {
                setInitRound(round)
                setCurrentRound(INIT_ROUND)
            }
            with(countViewModel) {
                setInitSquatCount(squatCount)
                setInitPushUpCount(pushUpCount)
                setCurrentSquatsCount(INIT_COUNT)
                setCurrentPushUpCount(INIT_COUNT)
            }

            val intent = Intent(this@MainActivity, StartRoundActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val INIT_ROUND = "1"
        private const val INIT_COUNT = 0
    }
}