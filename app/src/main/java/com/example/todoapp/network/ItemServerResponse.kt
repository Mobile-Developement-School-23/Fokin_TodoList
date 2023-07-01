package com.example.todoapp.network

data class ItemServerResponse(
    val element: TodoItemServer,
    val revision: Long
)
