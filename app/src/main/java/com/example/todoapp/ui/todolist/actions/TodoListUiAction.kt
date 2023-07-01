package com.example.todoapp.ui.todolist.actions

import com.example.todoapp.TodoItem

sealed class TodoListUiAction {
    data class EditTodoItem(val todoItem: TodoItem) : TodoListUiAction()
    data class UpdateTodoItem(val todoItem: TodoItem): TodoListUiAction()
    data class RemoveTodoItem(val todoItem: TodoItem) : TodoListUiAction()
}
