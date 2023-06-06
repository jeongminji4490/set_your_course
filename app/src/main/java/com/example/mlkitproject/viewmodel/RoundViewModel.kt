package com.example.mlkitproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoundViewModel : ViewModel() {

    private val dataStore: DataStoreModule = App.getInstance().getDataStore()

    var initRound: String = ""
        get() {
            viewModelScope.launch {
                field = dataStore.initRound.first()
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
            dataStore.setInitRound(round)
        }
    }


    @JvmName("setUserCurrentRound")
    fun setCurrentRound(currentRound: String) {
        viewModelScope.launch {
            dataStore.setCurrentRound(currentRound)
        }
    }

}