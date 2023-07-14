package com.example.todoapp.di

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
interface NotificationModule {
    companion object {
        @FragmentScope
        @Provides
        fun provideAlarmManager(@FragmentQualifier context: Context): AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }
}