package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Importance
import com.example.todoapp.data.TodoItem
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.di.FragmentScope
import com.example.todoapp.utils.dateToUnix
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@FragmentScope
class ChangeTodoItemViewModel(private val repository: TodoItemsRepository) : ViewModel() {
    var oldTodoItem: TodoItem? = null
    private lateinit var id: String
    var isNewItem: Boolean = true

    private val _navigation = Channel<ChangeTodoItemNavigations>()
    val navigation = _navigation.receiveAsFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private val _importance = MutableStateFlow(Importance.BASIC)
    val importance = _importance.asStateFlow()

    private val _deadline = MutableStateFlow(dateToUnix(Date()))
    val deadline = _deadline.asStateFlow()

    private val _isDeadlineSet = MutableStateFlow(false)
    val isDeadlineSet = _isDeadlineSet.asStateFlow()

    fun findTodoItem(args: ChangeTodoItemFragmentArgs) {
        viewModelScope.launch {
            id = args.id
            repository.getTodoItem(id)?.let { todoItem ->
                oldTodoItem = todoItem
                isNewItem = false
                updateText(todoItem.text)
                updateImportance(todoItem.importance)
                todoItem.deadline?.let {
                    updateDeadline(it)
                    updateIsDeadlineSet(true)
                }
            }
        }
    }

    fun updateText(text: String) {
        _text.update { text }
    }

    fun updateImportance(importance: Importance) {
        _importance.update { importance }
    }

    fun updateDeadline(deadline: Long) {
        _deadline.update { deadline }
    }

    fun updateIsDeadlineSet(isDeadlineSet: Boolean) {
        _isDeadlineSet.update { isDeadlineSet }
    }

    fun saveTodoItem() {
        val todoItem = if (isNewItem) {
            TodoItem(
                id = id,
                text = _text.value,
                importance = _importance.value,
                deadline = if (_isDeadlineSet.value) _deadline.value else null
            )
        } else {
            oldTodoItem!!.copy(
                text = _text.value,
                importance = _importance.value,
                deadline = if (_isDeadlineSet.value) _deadline.value else null,
                modificationDate = dateToUnix(Date())
            )
        }

        viewModelScope.launch {
            if (isNewItem) repository.addTodoItem(todoItem)
            else repository.updateTodoItem(todoItem)
            _navigation.send(ChangeTodoItemNavigations.NavigateUp)
        }
    }

    fun removeTodoItem() {
        viewModelScope.launch {
            if (!isNewItem)
                oldTodoItem?.let { repository.removeTodoItem(id) }
            _navigation.send(ChangeTodoItemNavigations.NavigateUp)
        }
    }
}