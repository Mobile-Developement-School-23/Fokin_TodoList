package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.network.MyWorkManager
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import com.example.todoapp.di.AppScope
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
    }
}