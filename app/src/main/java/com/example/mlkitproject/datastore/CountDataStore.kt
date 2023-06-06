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

class CountDataStore(private val context: Context) {

    private val Context.dataStore  by preferencesDataStore(name = "countDataStore")

    private val initSquatCountKey = intPreferencesKey("squat")
    private val currentSquatCountKey = intPreferencesKey("current_squat")
    private val initPushUpCountKey = intPreferencesKey("push_up")
    private val currentPushUpCountKey = intPreferencesKey("current_push_up")

    val initSquatCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[initSquatCountKey] ?: 0
        }


    suspend fun setInitSquatCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[initSquatCountKey] = value
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

    // PushUp initial count set by the user
    val initPushUpCount : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[initPushUpCountKey] ?: 0
        }

    suspend fun setInitPushUpCount(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[initPushUpCountKey] = value
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