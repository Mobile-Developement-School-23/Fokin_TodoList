package com.example.todoapp.network

import com.example.todoapp.network.TodoItemServer

data class TodoListContainer(
    val list: List<TodoItemServer>?
)