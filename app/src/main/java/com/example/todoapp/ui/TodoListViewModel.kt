package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Di
import com.example.todoapp.TodoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {
    private val todoItemsRepository = Di.basedRepository

    private val _uiEvent = Channel<TodoListNavigations>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val errorListLiveData: LiveData<Boolean> = todoItemsRepository.errorListLiveData
    val errorItemLiveData: LiveData<Boolean> = todoItemsRepository.errorItemLiveData

    fun onUiAction(action: TodoListUiAction) {
        when (action) {
            is TodoListUiAction.EditTodoItem -> editTodoItem(action.todoItem)
            is TodoListUiAction.UpdateTodoItem -> updateTodoItem(action.todoItem)
            is TodoListUiAction.RemoveTodoItem -> removeTodoItem(action.todoItem)
        }
    }

    suspend fun getTodoItems() = todoItemsRepository.todoItems()

    fun reloadData() {
        viewModelScope.launch {
            todoItemsRepository.reloadData()
        }
    }

    private fun editTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            _uiEvent.send(TodoListNavigations.NavigateToEditTodoItem(todoItem.id))
        }
    }

    private fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.updateTodoItem(todoItem)
        }
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemsRepository.removeTodoItem(todoItem.id)
        }
    }
}