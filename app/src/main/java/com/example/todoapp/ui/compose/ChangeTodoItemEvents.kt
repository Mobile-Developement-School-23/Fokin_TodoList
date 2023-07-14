package com.example.todoapp.ui.compose

sealed class ChangeTodoItemEvents {
    object NavigateUp: ChangeTodoItemEvents()
    object SaveTodoItem: ChangeTodoItemEvents()
}
