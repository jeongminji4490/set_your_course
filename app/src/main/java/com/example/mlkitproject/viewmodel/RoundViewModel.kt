package com.example.mlkitproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoundViewModel : ViewModel() {

    private val dataStore: DataStoreModule = App.getInstance().getDataStore()

    // would it be better to use this way .. ?
    var initRound: String = ""
        get() {
            viewModelScope.launch {
                field = dataStore.round.first()
            }
            return field
        }

    // change collect to first
    var currentRound: String = ""
        get() {
            viewModelScope.launch {
                dataStore.currentRound.collect {
                    field = it
                }
            }
            return field
        }

    @JvmName("setUserInitRound")
    fun setInitRound(round: String) {
        viewModelScope.launch {
            dataStore.setRound(round)
        }
    }


    @JvmName("setUserCurrentRound")
    fun setCurrentRound(currentRound: String) {
        viewModelScope.launch {
            dataStore.setCurrentRound(currentRound)
        }
    }

}