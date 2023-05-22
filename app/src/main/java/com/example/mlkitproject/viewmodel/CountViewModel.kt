package com.example.mlkitproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountViewModel() : ViewModel() {

    // apply nullable to properties?
    private val dataStore: DataStoreModule = App.getInstance().getDataStore()

    var currentRound: String = ""
    var requiredRound: String = ""
    var requiredSquatCount: Int = 0
    var requiredPushUpCount: Int = 0

    /**
     * Better to expose immutable types to other classes
     * All changes to the mutable type is centralized in one class making it easier to debug when something goes wrong
     */
    private var _currentSquatsCount = MutableLiveData<Int>()
    val currentSquatsCount: LiveData<Int>
        get() = _currentSquatsCount

    private var _currentPushUpCount = MutableLiveData<Int>()
    val currentPushUpCount: LiveData<Int>
        get() =  _currentPushUpCount

    init {
        getRequiredRound()
        getCurrentRound()
        getRequiredSquatCountValue()
        getRequiredPushUpCountValue()
    }

    /**
     * TODO
     * I think I need to make setter, not only getter
     * and it would be better to separate viewModel by features
     */

    private fun getRequiredRound() {
        viewModelScope.launch {
            requiredRound = dataStore.round.first()
        }
    }

    private fun getCurrentRound() {
        viewModelScope.launch {
            currentRound = dataStore.currentRound.first()
        }
    }

    /**
     * ViewModel classes should prefer creating coroutines instead of exposing suspend functions to perform business logic
     */
    private fun getRequiredSquatCountValue() {
        viewModelScope.launch {
            requiredSquatCount = dataStore.squatCount.first()
        }
    }

    fun getCurrentSquatsCountValue() {
        viewModelScope.launch {
            dataStore.currentSquatCount.collect {
                _currentSquatsCount.value = it
            }
        }
    }

    private fun getRequiredPushUpCountValue() {
        viewModelScope.launch {
            requiredPushUpCount = dataStore.pushUpCount.first()
        }
    }

    fun getCurrentPushUpCountValue() {
        viewModelScope.launch {
            dataStore.currentPushUpCount.collect {
                _currentPushUpCount.value = it
            }
        }
    }

}