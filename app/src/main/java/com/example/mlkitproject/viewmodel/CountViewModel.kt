package com.example.mlkitproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountViewModel : ViewModel() {

    private val dataStore: DataStoreModule = App.getInstance().getDataStore()

    var initSquatCount: Int = 0
    var initPushUpCount: Int = 0

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
        getInitSquatCount()
        getInitPushUpCount()
    }

    /**
     * ViewModel classes should prefer creating coroutines instead of exposing suspend functions to perform business logic
     */

    /**
     * Squat
     */
    @JvmName("setInitialCountForSquat")
    fun setInitSquatCount(count: Int) {
        viewModelScope.launch {
            dataStore.setSquatCount(count)
        }
    }

    private fun getInitSquatCount() {
        viewModelScope.launch {
            initSquatCount = dataStore.squatCount.first()
        }
    }

    fun setCurrentSquatsCount(count: Int) {
        viewModelScope.launch {
            dataStore.setCurrentSquatCount(count)
        }
    }

    fun getCurrentSquatsCount() {
        viewModelScope.launch {
            dataStore.currentSquatCount.collect {
                _currentSquatsCount.value = it
            }
        }
    }

    /**
     * Push up
     */
    @JvmName("setInitialCountForPushUp")
    fun setInitPushUpCount(count: Int) {
        viewModelScope.launch {
            dataStore.setPushUpCount(count)
        }
    }

    private fun getInitPushUpCount() {
        viewModelScope.launch {
            initPushUpCount = dataStore.pushUpCount.first()
        }
    }

    fun setCurrentPushUpCount(count: Int) {
        viewModelScope.launch {
            dataStore.setCurrentPushUpCount(count)
        }
    }

    fun getCurrentPushUpCount() {
        viewModelScope.launch {
            dataStore.currentPushUpCount.collect {
                _currentPushUpCount.value = it
            }
        }
    }

}