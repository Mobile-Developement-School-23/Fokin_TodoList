package com.example.todoapp.ui.todolist.actions

sealed class TodoListUiEvent {
    data class NavigateToEditTodoItem(val id: String): TodoListUiEvent()
    object NavigateToNewTodoItem: TodoListUiEvent()
}
