/*
 * Copyright GE Appliances, a Haier Company (Confidential). All rights reserved.
 */

package com.example.mlkitproject

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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
    // private val exerciseTypeKey = stringPreferencesKey("exercise_type")
    private val squatCountKey = stringPreferencesKey("squat")
    private val pushUpCountKey = stringPreferencesKey("push_up")

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

//    val exerciseType : Flow<String> = context.dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preferences ->
//            preferences[exerciseTypeKey] ?: ""
//        }
//
//    suspend fun setExerciseType(type: String) {
//        context.dataStore.edit { preferences ->
//            preferences[exerciseTypeKey] = type
//        }
//    }

    val squatCount : Flow<String> = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[squatCountKey] ?: ""
            }


    suspend fun setSquatCount(value: String) {
        context.dataStore.edit { preferences ->
            preferences[squatCountKey] = value
        }
    }

    val pushUpCount : Flow<String> = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {preferences ->
                preferences[pushUpCountKey] ?: ""
            }

    suspend fun setPushUpCount(value: String) {
        context.dataStore.edit { preferences ->
            preferences[pushUpCountKey] = value
        }
    }
}