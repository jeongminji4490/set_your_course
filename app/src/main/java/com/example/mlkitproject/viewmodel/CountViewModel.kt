package com.example.mlkitproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.datastore.CountDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @file CountViewModel.kt
 * @author jeongminji4490
 * @brief this is the viewmodel class that handles the business logics related to CountDataStore
 */
class CountViewModel : ViewModel() {

    private val dataStore: CountDataStore = App.getInstance().getCountDataStore()
    private val dataStoreScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    var targetSquatCount: Int = 0
        get() {
            viewModelScope.launch {
                field = dataStore.targetSquatCount.first()
            }
            return field
        }
    var targetPushUpCount: Int = 0
        get() {
            viewModelScope.launch {
                field = dataStore.targetPushUpCount.first()
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
    @JvmName("setTargetCountForSquat")
    fun setTargetSquatCount(count: Int) {
        viewModelScope.launch {
            dataStore.setTargetSquatCount(count)
        }
    }

    @JvmName("setTargetCountForPushUp")
    fun setTargetPushUpCount(count: Int) {
        viewModelScope.launch {
            dataStore.setTargetPushUpCount(count)
        }
    }

    /**
     * @Note
     * There was a problem with cancelling datastore's suspend function
     * Because viewmodel instance is destroyed before the datastore's suspend function is executed
     * So I applied the custom scope that is not affected by viewmodel lifecycle
     */
    fun setCurrentSquatsCount(count: Int) {
        dataStoreScope.launch {
            dataStore.setCurrentSquatCount(count)
        }
    }

    fun setCurrentPushUpCount(count: Int) {
        dataStoreScope.launch {
            dataStore.setCurrentPushUpCount(count)
        }
    }

}