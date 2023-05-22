package com.example.mlkitproject

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.mlkitproject.databinding.FragmentCompleteDialogBinding
import com.example.mlkitproject.viewmodel.CountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CompleteDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentCompleteDialogBinding

    private val dataStore = App.getInstance().getDataStore()
    private val viewModel: CountViewModel by viewModels()
    private var requiredRound: String = ""
    private var currentRound: String = ""

    companion object {
        private var dialogInstance: CompleteDialogFragment = CompleteDialogFragment()
        fun getInstance() = dialogInstance
    }

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

        binding.btnGoNextRound.setOnClickListener {
            val intent = Intent(requireActivity(), DetectPoseActivity::class.java)
            intent.putExtra("fragment_type", "push_up")
            startActivity(intent)
            this@CompleteDialogFragment.dismiss()
        }

//        binding.btnFinish.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                val requiredRound = dataStore.round.first()
//                val currentRound = dataStore.currentRound.first()
//                Log.e("Jinnie_requiredRound", requiredRound)
//                Log.e("Jinnie_currentRound", currentRound)
//
//                if (currentRound == requiredRound) {
//                    val intent = Intent(requireActivity(), MainActivity::class.java)
//                    startActivity(intent)
//                    this@CompleteDialogFragment.dismiss()
//                } else {
//                    dataStore.run {
//                        setCurrentSquatCount(0)
//                        setCurrentPushUpCount(0)
//                        val nextRound = (currentRound.toInt() + 1).toString()
//                        dataStore.setCurrentRound(nextRound)
//                    }
//                    val intent = Intent(requireActivity(), StartRoundActivity::class.java)
//                    startActivity(intent)
//                    this@CompleteDialogFragment.dismiss()
//                }
//
//
//            }
//
//        }

        binding.btnFinish.setOnClickListener {

            if (currentRound == requiredRound) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                this@CompleteDialogFragment.dismiss()
            } else {

            }
        }

        requiredRound = viewModel.requiredRound
        currentRound = viewModel.currentRound

    }

}