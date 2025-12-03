package com.example.ecommapp

import android.app.Application
import com.example.ecommapp.data.local.AppDatabase

class EcommApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}