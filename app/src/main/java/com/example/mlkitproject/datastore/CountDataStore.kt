package com.example.mlkitproject.datastore

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

/**
 * @file CountDataStore.kt
 * @author jeongminji4490
 * @brief This is the PreferencesDataStore class that saves target & current count
 */
class CountDataStore(private val context: Context) {

    private val Context.dataStore  by preferencesDataStore(name = "countDataStore")

    private val targetSquatCountKey = intPreferencesKey("squat")
    private val currentSquatCountKey = intPreferencesKey("current_squat")
    private val targetPushUpCountKey = intPreferencesKey("push_up")
    private val currentPushUpCountKey = intPreferencesKey("current_push_up")

    val targetSquatCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[targetSquatCountKey] ?: 0
        }


    suspend fun setTargetSquatCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[targetSquatCountKey] = value
        }
    }

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

    val targetPushUpCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[targetPushUpCountKey] ?: 0
        }

    suspend fun setTargetPushUpCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[targetPushUpCountKey] = value
        }
    }


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