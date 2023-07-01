package com.example.todoapp.network

import com.example.todoapp.network.TodoItemServer

data class TodoListServerResponse(
    val list: List<TodoItemServer>?,
    val revision: Long
)
