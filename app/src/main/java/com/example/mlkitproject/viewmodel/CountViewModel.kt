package com.example.mlkitproject.viewmodel

import android.util.AndroidException

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mlkitproject.App
import com.example.mlkitproject.DataStoreModule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountViewModel(context: Application) : AndroidViewModel(context) {

    private val dataStore: DataStoreModule = App.getInstance().getDataStore()
    var currentRound : String = ""
    // var lastRound : String = ""
    // var exerciseType = MutableLiveData<String>()
    var pushupCount = MutableLiveData<String>()
    var squatsCount = MutableLiveData<String>()

    init {
        // exerciseType.value = ""
        pushupCount.value = "0"
        squatsCount.value = "0"
    }

    suspend fun getCurrentRound() {
        viewModelScope.launch {
            dataStore.currentRound.collect {
                currentRound = it
            }
        }
    }

//    suspend fun getExerciseType () {
//        viewModelScope.launch {
//            dataStore.exerciseType.collect {
//                exerciseType.value = it
//            }
//        }
//    }

    suspend fun getPushUpCountValue() {
        viewModelScope.launch {
            dataStore.pushUpCount.collect {
                pushupCount.value = it
            }
        }
    }

    suspend fun getSquatsCountValue() {
        viewModelScope.launch {
            dataStore.squatCount.collect {
                squatsCount.value = it
            }
        }
    }
}