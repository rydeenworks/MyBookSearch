package com.rydeenworks.mybooksearch

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        private lateinit var _instance: Application private set
    }

    fun GetInstance() : Application {
        return _instance
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "hello MainApp")
        _instance = this
    }
}