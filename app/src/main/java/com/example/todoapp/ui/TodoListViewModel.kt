package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoItem
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.di.FragmentScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@FragmentScope
class TodoListViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {
    private val _navigation = Channel<TodoListNavigations>()
    val navigation = _navigation.receiveAsFlow()

    fun onUiAction(action: TodoListActions) {
        when (action) {
            is TodoListActions.EditTodoItem -> editTodoItem(action.todoItem)
            is TodoListActions.UpdateTodoItem -> updateTodoItem(action.todoItem)
            is TodoListActions.RemoveTodoItem -> removeTodoItem(action.todoItem)
        }
    }

    suspend fun getTodoItems() = repository.todoItems()

    suspend fun getUnfinishedTodoItems() = repository.unfinishedTodoItems()

    fun errorListLiveData(): LiveData<Boolean> = repository.errorListLiveData()

    fun errorItemLiveData(): LiveData<Boolean> = repository.errorItemLiveData()

    fun reloadData() {
        viewModelScope.launch {
            repository.reloadData()
        }
    }

    private fun editTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            _navigation.send(TodoListNavigations.NavigateToEditTodoItem(todoItem.id))
        }
    }

    private fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateTodoItem(todoItem)
        }
    }

    private fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.removeTodoItem(todoItem.id)
        }
    }
}