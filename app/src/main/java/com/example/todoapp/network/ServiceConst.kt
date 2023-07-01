package com.example.todoapp.network

object ServiceConst {
    private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    val service: Service
        get() = Client.getClient(BASE_URL).create(Service::class.java)
}