package com.example.todoapp.data.network

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.todoapp.di.AppScope
import com.example.todoapp.utils.RETRY_COUNT
import okhttp3.Interceptor
import okhttp3.Response

@AppScope
class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var responseOK = false
        var tryCount = 0

        while (!responseOK && tryCount < RETRY_COUNT) {
            try {
                response = chain.proceed(request)
                responseOK = response.isSuccessful
            } catch (e: NetworkErrorException) {
                Log.d("interception", "Network problems, number of requests try - $tryCount")
            } finally {
                tryCount++
            }
        }

        return response!!
    }
}