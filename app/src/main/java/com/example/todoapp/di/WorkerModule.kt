package com.example.todoapp.di

import dagger.Module
import dagger.Provides
import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.example.todoapp.data.network.MyWorkManager

@Module
interface WorkerModule {
    companion object {
        @AppScope
        @Provides
        fun provideWorkManager(
            workManager: WorkManager,
            connectivityManager: ConnectivityManager
        ): MyWorkManager {
            return MyWorkManager(workManager, connectivityManager)
        }
        @AppScope
        @Provides
        fun provideWorkManagerInstance(context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }

        @AppScope
        @Provides
        fun provideConnectivityManager(context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }
}