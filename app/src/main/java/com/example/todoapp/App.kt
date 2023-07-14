package com.example.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.todoapp.data.network.MyWorkManager
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import com.example.todoapp.di.AppScope
import com.example.todoapp.utils.CHANNEL_ID
import com.example.todoapp.utils.CHANNEL_NAME
import javax.inject.Inject

@AppScope
class App : Application() {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workManager: MyWorkManager

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(applicationContext)
        appComponent.inject(this)
        workManager.setWorkers()

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}