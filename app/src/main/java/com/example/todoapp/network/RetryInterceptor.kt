package com.example.todoapp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var responseOK = false
        var tryCount = 0

        while (!responseOK && tryCount < 3) {
            try {
                response = chain.proceed(request)
                responseOK = response.isSuccessful
            } catch (e: Exception) {
                Log.d("intercept", "Request is not successful - $tryCount")
            } finally {
                tryCount++
            }
        }

        return response!!
    }
}