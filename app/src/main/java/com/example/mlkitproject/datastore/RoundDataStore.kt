package com.example.mlkitproject.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * @file RoundDataStore.kt
 * @author jeongminji4490
 * @brief This is the PreferencesDataStore class that saves target & current round
 */
class RoundDataStore(private val context: Context) {

    private val Context.dataStore  by preferencesDataStore(name = "roundDataStore")

    private val currentRoundKey = stringPreferencesKey("current_round")
    private val targetRoundKey = stringPreferencesKey("round")

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

    val targetRound : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[targetRoundKey] ?: ""
        }

    suspend fun setTargetRound(num: String) {
        context.dataStore.edit { preferences ->
            preferences[targetRoundKey] = num
        }
    }
}