package com.example.todoapp.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.Di

class InaccessibleNetworkWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val repository = Di.basedRepository

    override suspend fun doWork(): Result {
        repository.loadDataFromDB()
        return Result.success()
    }
}