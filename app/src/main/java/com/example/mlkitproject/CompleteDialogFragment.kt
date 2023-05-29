package com.example.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mlkitproject.databinding.FragmentCompleteDialogBinding
import com.example.mlkitproject.viewmodel.CountViewModel
import com.example.mlkitproject.viewmodel.RoundViewModel

class CompleteDialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var binding : FragmentCompleteDialogBinding

    private val countViewModel: CountViewModel by viewModels()
    private lateinit var roundViewModel: RoundViewModel
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

        roundViewModel = ViewModelProvider(this)[RoundViewModel::class.java]

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_go_next_round -> {
                goToNextExerciseActivity()
            }
            R.id.btn_finish -> {
                if (currentRound == requiredRound) {
                    goToMainActivity()
                } else {
                    setNextRoundAndCount()
                    goToStartRoundActivity()
                }
            }
        }
    }

    private fun goToNextExerciseActivity() {
        val intent = Intent(requireActivity(), DetectPoseActivity::class.java)
        intent.putExtra("fragment_type", "push_up")
        startActivity(intent)
        this@CompleteDialogFragment.dismiss()
    }

    private fun goToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        this@CompleteDialogFragment.dismiss()
    }

    private fun goToStartRoundActivity() {
        val intent = Intent(requireActivity(), StartRoundActivity::class.java)
        startActivity(intent)
        this@CompleteDialogFragment.dismiss()
    }

    private fun setNextRoundAndCount() {
        val nextRound = (currentRound.toInt() + 1).toString()
        roundViewModel.setCurrentRound(nextRound)
        countViewModel.setCurrentSquatsCount(INIT_COUNT)
        countViewModel.setCurrentPushUpCount(INIT_COUNT)
    }

    companion object {
        private var dialogInstance: CompleteDialogFragment = CompleteDialogFragment()
        fun getInstance() = dialogInstance

        const val INIT_COUNT = 0
    }

}