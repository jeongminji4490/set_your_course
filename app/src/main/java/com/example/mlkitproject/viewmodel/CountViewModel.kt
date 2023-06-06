package com.example.mlkitproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountViewModel : ViewModel() {

    private val dataStore: DataStoreModule = App.getInstance().getDataStore()
    private val dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var initSquatCount: Int = 0
        get() {
            viewModelScope.launch {
                field = dataStore.initSquatCount.first()
            }
            return field
        }
    var initPushUpCount: Int = 0
        get() {
            viewModelScope.launch {
                field = dataStore.initPushUpCount.first()
            }
            return field
        }

    /**
     * Better to expose immutable types to other classes
     * All changes to the mutable type is centralized in one class making it easier to debug when something goes wrong
     */
    private var _currentSquatCount = MutableLiveData<Int>()
        get() {
            viewModelScope.launch {
                dataStore.currentSquatCount.collect {
                    field.value = it
                }
            }
            return field
        }

    val currentSquatsCount: LiveData<Int>
        get() = _currentSquatCount

    private var _currentPushUpCount = MutableLiveData<Int>()
        get() {
            viewModelScope.launch {
                dataStore.currentPushUpCount.collect {
                    field.value = it
                }
            }
            return field
        }
    val currentPushUpCount: LiveData<Int>
        get() =  _currentPushUpCount

    /**
     * ViewModel classes should prefer creating coroutines instead of exposing suspend functions to perform business logic
     */

    /**
     * Squat
     */
    @JvmName("setInitialCountForSquat")
    fun setInitSquatCount(count: Int) {
        viewModelScope.launch {
            dataStore.setInitSquatCount(count)
        }
    }

    fun setCurrentSquatsCount(count: Int) {
        dataStoreScope.launch {
            dataStore.setCurrentSquatCount(count)
        }
    }

    /**
     * Push up
     */
    @JvmName("setInitialCountForPushUp")
    fun setInitPushUpCount(count: Int) {
        viewModelScope.launch {
            dataStore.setInitPushUpCount(count)
        }
    }

    fun setCurrentPushUpCount(count: Int) {
        dataStoreScope.launch {
            dataStore.setCurrentPushUpCount(count)
        }
    }

}