package com.example.todoapp

import android.app.Application
import com.example.todoapp.network.WorkManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Di.init(applicationContext)
        WorkManager.setWorkers(applicationContext)
    }
}