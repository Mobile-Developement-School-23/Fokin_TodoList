package com.example.todoapp.data.network

data class TodoListServerResponse(val list: List<TodoItemFromServer>?, val revision: Long)
