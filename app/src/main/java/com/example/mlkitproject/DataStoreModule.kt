/*
 * Copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */

package com.example.mlkitproject

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context: Context) {

    private val Context.dataStore  by preferencesDataStore(name = "dataStore")

    private val currentRoundKey = stringPreferencesKey("current_round")
    private val roundKey = stringPreferencesKey("round")
    private val squatCountKey = intPreferencesKey("squat")
    private val currentSquatCountKey = intPreferencesKey("current_squat")
    private val pushUpCountKey = intPreferencesKey("push_up")
    private val currentPushUpCountKey = intPreferencesKey("current_push_up")

    val currentRound : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[currentRoundKey] ?: ""
        }

    suspend fun setCurrentRound(num: String) {
        context.dataStore.edit { preferences ->
            preferences[currentRoundKey] = num
        }
    }

    val round : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[roundKey] ?: ""
        }

    suspend fun setRound(num: String) {
        context.dataStore.edit { preferences ->
            preferences[roundKey] = num
        }
    }

    // Squat initial count set by the user
    val squatCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[squatCountKey] ?: 0
        }


    suspend fun setSquatCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[squatCountKey] = value
        }
    }


//    val squatCount : Flow<String> = context.dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map {preferences ->
//                preferences[squatCountKey] ?: ""
//            }
//
//
//    suspend fun setSquatCount(value: String) {
//        context.dataStore.edit { preferences ->
//            preferences[squatCountKey] = value
//        }
//    }

    val currentSquatCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[currentSquatCountKey] ?: 0
        }


    suspend fun setCurrentSquatCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[currentSquatCountKey] = value
        }
    }

    // PushUp initial count set by the user
    val pushUpCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[pushUpCountKey] ?: 0
        }

    suspend fun setPushUpCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[pushUpCountKey] = value
        }
    }

//    val pushUpCount : Flow<String> = context.dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map {preferences ->
//                preferences[pushUpCountKey] ?: ""
//            }
//
//    suspend fun setPushUpCount(value: String) {
//        context.dataStore.edit { preferences ->
//            preferences[pushUpCountKey] = value
//        }
//    }

    val currentPushUpCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[currentPushUpCountKey] ?: 0
        }

    suspend fun setCurrentPushUpCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[currentPushUpCountKey] = value
        }
    }

}