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
 * for save values related exercise count
 * this viewmodel handles the business logics related to CountDataStore
 * initCount -> initial count set by the user
 * currentCount -> initial count set by the user
 * these are used to check if the user completes the exercise
 */
class CountViewModel : ViewModel() {

    private val dataStore: CountDataStore = App.getInstance().getCountDataStore()
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

    @JvmName("setInitialCountForSquat")
    fun setInitSquatCount(count: Int) {
        viewModelScope.launch {
            dataStore.setInitSquatCount(count)
        }
    }

    @JvmName("setInitialCountForPushUp")
    fun setInitPushUpCount(count: Int) {
        viewModelScope.launch {
            dataStore.setInitPushUpCount(count)
        }
    }

    /**
     * There is a problem with cancelling datastore's suspend function
     * Because viewmodel instance is destroyed before the suspend function is executed
     * So I applied custom scope that is not affected by viewmodel lifecycle
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