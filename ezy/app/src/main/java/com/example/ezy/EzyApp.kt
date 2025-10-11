package com.example.ezy

import android.app.Application

class EzyApp : Application() {
    companion object {
        lateinit var instance: EzyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}