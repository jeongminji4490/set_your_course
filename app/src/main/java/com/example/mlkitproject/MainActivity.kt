package com.example.mlkitproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mlkitproject.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var splashScreen: androidx.core.splashscreen.SplashScreen
    private val dataStore = App.getInstance().getDataStore()

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
                var num = binding.txSetCount.text.toString().toInt()
                num += 1
                if (num > 30) {
                    Log.i(TAG, "exceeded maximum number")
                } else {
                    binding.txSetCount.text = num.toString()
                }
            }
            R.id.btn_down_count -> {
                var num = binding.txSetCount.text.toString().toInt()
                num -= 1
                if (num < 1) {
                    Log.i(TAG, "0 times not allowed")
                } else {
                    binding.txSetCount.text = num.toString()
                }
            }
            R.id.btn_start -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val squatCount = binding.edtSquatCount.text.toString()
                    val pushUpCount = binding.edtPushUpCount.text.toString()
                    /**
                     * TODO
                     * numbers only
                     * If the input is not number, I need to throw an exception about it
                     */
                    if (squatCount == "" || pushUpCount == "") {
                        Toast.makeText(this@MainActivity, "Please enter the count", Toast.LENGTH_SHORT).show()
                    } else {
                        val setCount = binding.txSetCount.text.toString()
                        dataStore.run {
                            // setExerciseType("Squat")
                            setSquatCount(squatCount)
                            setPushUpCount(pushUpCount)
                            setRound(setCount)
                            setCurrentRound("1")
                        }
                        val intent = Intent(this@MainActivity, StartRoundActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}