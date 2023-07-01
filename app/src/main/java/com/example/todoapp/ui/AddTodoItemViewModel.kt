package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Di
import com.example.todoapp.Importance
import com.example.todoapp.TodoItem
import com.example.todoapp.utils.dateToUnix
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddTodoItemViewModel : ViewModel() {
    private val todoItemsRepository = Di.basedRepository

    private var oldTodoItem: TodoItem? = null
    private lateinit var id: String
    private var isNewItem: Boolean = true

    private val _uiEvent = Channel<AddTodoItemUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private val _importance = MutableStateFlow(Importance.LOW)
    val importance = _importance.asStateFlow()

    private val _deadline = MutableStateFlow(dateToUnix(Date()))
    val deadline = _deadline.asStateFlow()

    private val _isDeadlineSet = MutableStateFlow(false)
    val isDeadlineSet = _isDeadlineSet.asStateFlow()

    fun findTodoItem(args: com.example.todoapp.ui.AddTodoItemFragmentArgs) {
        viewModelScope.launch {
            id = args.id
            todoItemsRepository.getTodoItem(id)?.let { todoItem ->
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
            if (isNewItem) todoItemsRepository.addTodoItem(todoItem)
            else todoItemsRepository.updateTodoItem(todoItem)
            _uiEvent.send(AddTodoItemUiEvent.NavigateUp)
        }
    }

    fun removeTodoItem() {
        viewModelScope.launch {
            if (!isNewItem)
                oldTodoItem?.let { todoItemsRepository.removeTodoItem(id) }
            _uiEvent.send(AddTodoItemUiEvent.NavigateUp)
        }
    }
}