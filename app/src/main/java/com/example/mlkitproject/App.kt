package com.example.mlkitproject

import android.app.Application

class App : Application() {

    private lateinit var dataStoreModule: DataStoreModule

    companion object {
        private lateinit var app : App
        fun getInstance() : App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        dataStoreModule = DataStoreModule(this)
    }

    fun getDataStore() : DataStoreModule = dataStoreModule
}