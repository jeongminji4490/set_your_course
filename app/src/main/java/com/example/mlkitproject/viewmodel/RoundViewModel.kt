package com.example.mlkitproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.datastore.RoundDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @file RoundViewModel.kt
 * @author jeongminji4490
 * @brief this is the viewmodel class that handles the business logics related to RoundDataStore
 */
class RoundViewModel : ViewModel() {

    private val dataStore: RoundDataStore = App.getInstance().getRoundDataStore()

    var initRound: String = ""
        get() {
            viewModelScope.launch {
                field = dataStore.targetRound.first()
            }
            return field
        }

    var currentRound: String = ""
        get() {
            viewModelScope.launch {
                field = dataStore.currentRound.first()
            }
            return field
        }

    @JvmName("setUserInitRound")
    fun setInitRound(round: String) {
        viewModelScope.launch {
            dataStore.setTargetRound(round)
        }
    }


    @JvmName("setUserCurrentRound")
    fun setCurrentRound(currentRound: String) {
        viewModelScope.launch {
            dataStore.setCurrentRound(currentRound)
        }
    }

}