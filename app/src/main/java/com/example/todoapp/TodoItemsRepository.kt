package com.example.todoapp

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    suspend fun todoItems(): Flow<List<TodoItem>>
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun removeTodoItem(id: String)
}