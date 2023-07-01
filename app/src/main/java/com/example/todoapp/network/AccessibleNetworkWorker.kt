package com.example.todoapp.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.Di

class AccessibleNetworkWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {
    private val repository = Di.basedRepository

    override suspend fun doWork(): Result {
        repository.loadDataFromServer()
        return Result.success()
    }
}