package com.example.mlkitproject

import android.app.Application
import com.example.mlkitproject.datastore.CountDataStore
import com.example.mlkitproject.datastore.RoundDataStore

/**
 * @file App.kt
 * @author jeongminji4490
 * @brief This is Application class that initiates and returns datastore instance
 */
class App : Application() {

    private lateinit var roundDataStore: RoundDataStore
    private lateinit var countDataStore: CountDataStore

    companion object {
        private lateinit var app : App
        fun getInstance() : App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        roundDataStore = RoundDataStore(this)
        countDataStore = CountDataStore(this)
    }

    fun getRoundDataStore(): RoundDataStore = roundDataStore
    fun getCountDataStore(): CountDataStore = countDataStore
}