package com.example.todoapp.data.network

import com.example.todoapp.di.AppScope
import okhttp3.Interceptor
import okhttp3.Response

@AppScope
class AuthenticationInterceptor(private val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", token)

        return chain.proceed(requestBuilder.build())
    }
}