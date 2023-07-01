package com.example.todoapp.ui

import com.example.todoapp.TodoItem

sealed class TodoListActions {
    data class EditTodoItem(val todoItem: TodoItem) : TodoListActions()
    data class UpdateTodoItem(val todoItem: TodoItem): TodoListActions()
    data class RemoveTodoItem(val todoItem: TodoItem) : TodoListActions()
}
