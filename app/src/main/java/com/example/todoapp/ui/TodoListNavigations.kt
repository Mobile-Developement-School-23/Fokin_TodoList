package com.example.todoapp.ui

sealed class TodoListNavigations {
    data class NavigateToEditTodoItem(val id: String): TodoListNavigations()
    object NavigateToNewTodoItem: TodoListNavigations()
}
