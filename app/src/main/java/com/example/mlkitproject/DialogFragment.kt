package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.mlkitproject.databinding.FragmentCompleteDialogBinding
import com.example.mlkitproject.viewmodel.CountViewModel
import com.example.mlkitproject.viewmodel.RoundViewModel

/**
 * @file DialogFragment.kt
 * @author jeongminji4490
 * @brief This is the DialogFragment that is displayed when the user has completed exercise
 */
class DialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var binding : FragmentCompleteDialogBinding

    private val countViewModel: CountViewModel by viewModels()
    private val roundViewModel: RoundViewModel by viewModels()
    private var requiredRound: String = ""
    private var currentRound: String = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteDialogBinding.inflate(inflater)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnType = arguments?.getString("button_type")

        if (btnType == "next") {
            binding.btnGoNextRound.visibility = View.VISIBLE
        } else {
            binding.btnFinish.visibility = View.VISIBLE
        }

        requiredRound = roundViewModel.initRound
        currentRound = roundViewModel.currentRound

        binding.btnGoNextRound.setOnClickListener(this)
        binding.btnFinish.setOnClickListener(this)

    }

    /**
     * if the user has finished squat, move to PushUpFragment for the user to do next exercise
     * if the user has finished pushup, move to RoundActivity for the user to start next round
     * if the user has finished all sets, move to MainActivity
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_go_next_round -> {
                goToNextExerciseActivity()
            }
            R.id.btn_finish -> {
                if (currentRound == requiredRound) { // when the user has completed all sets
                    goToMainActivity()
                } else {
                    setNextRoundAndCount()
                    goToStartRoundActivity()
                }
            }
        }
    }

    private fun goToNextExerciseActivity() {
        val intent = Intent(requireActivity(), PoseActivity::class.java)
        intent.putExtra("fragment_type", "push_up")
        startActivity(intent)
        this@DialogFragment.dismiss()
    }

    private fun goToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        this@DialogFragment.dismiss()
    }

    /**
     * set the next round value and reset the current count
     */
    private fun setNextRoundAndCount() {
        val nextRound = (currentRound.toInt() + 1).toString()
        roundViewModel.setCurrentRound(nextRound)
        countViewModel.setCurrentSquatsCount(INIT_COUNT)
        countViewModel.setCurrentPushUpCount(INIT_COUNT)
    }

    private fun goToStartRoundActivity() {
        val intent = Intent(requireActivity(), RoundActivity::class.java)
        startActivity(intent)
        this@DialogFragment.dismiss()
    }

    companion object {
        const val INIT_COUNT = 0
    }

}