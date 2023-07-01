package com.example.todoapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkManager {
    private lateinit var workManager: WorkManager
    private lateinit var connectivityManager: ConnectivityManager

    fun setWorkers(context: Context) {
        workManager = WorkManager.getInstance(context)
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        refreshPeriodicWork()
        loadDataWork()
    }

    fun reloadData() {
        loadDataWork()
    }

    private fun loadDataWork() {
        loadDataFromServer()
        if (!isNetworkAvailable()) {
            loadDataFromDB()
        }
    }

    fun isNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun refreshPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequest.Builder(DataCheckWorker::class.java, 8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "refreshWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    private fun loadDataFromServer() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequest.Builder(AccessibleNetworkWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniqueWork(
                "loadServerWorker",
                ExistingWorkPolicy.KEEP,
                request
            )
    }

    private fun loadDataFromDB() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val request = OneTimeWorkRequest.Builder(InaccessibleNetworkWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager
            .enqueueUniqueWork(
                "loadDBWorker",
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}