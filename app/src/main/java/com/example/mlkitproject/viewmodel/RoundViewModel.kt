package com.example.mlkitproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.datastore.RoundDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * for save values related to round
 * this viewmodel handles the business logics related to RoundDataStore
 * initRound -> initial round set by the user
 * current Round -> round that the user is currently in
 * these values are used to check that the current round is next one or final one
 */
class RoundViewModel : ViewModel() {

    private val dataStore: RoundDataStore = App.getInstance().getRoundDataStore()

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