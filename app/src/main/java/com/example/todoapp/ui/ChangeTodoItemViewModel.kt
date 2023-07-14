package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Importance
import com.example.todoapp.data.TodoItem
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.di.FragmentScope
import com.example.todoapp.ui.compose.ChangeTodoItemActions
import com.example.todoapp.ui.compose.ChangeTodoItemEvents
import com.example.todoapp.ui.compose.ChangeTodoItemState

import com.example.todoapp.utils.dateToUnix
import com.example.todoapp.utils.unixToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@FragmentScope
class ChangeTodoItemViewModel(private val repository: TodoItemsRepository) : ViewModel() {
    private var oldTodoItem: TodoItem? = null
    private lateinit var id: String
    private var isNewItem: Boolean = true

    private val _uiEvent = Channel<ChangeTodoItemEvents>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(ChangeTodoItemState())
    val uiState = _uiState.asStateFlow()

    fun init(args: ChangeTodoItemFragmentArgs) {
        viewModelScope.launch {
            id = args.id
            repository.getTodoItem(id)?.let { todoItem ->
                oldTodoItem = todoItem
                isNewItem = false

                _uiState.update { uiState.value.copy(
                    text = todoItem.text,
                    importance = todoItem.importance,
                    deadline = if (todoItem.deadline != null)
                        unixToDate(todoItem.deadline!!) else uiState.value.deadline,
                    isDeadlineSet = todoItem.deadline != null,
                    isNewItem = false
                ) }
            }
        }
    }

    fun onUiAction(action: ChangeTodoItemActions) {
        when(action) {
            ChangeTodoItemActions.Save -> saveTodoItem()
            ChangeTodoItemActions.Delete -> removeTodoItem()
            ChangeTodoItemActions.NavigateUp -> viewModelScope.launch {
                _uiEvent.send(ChangeTodoItemEvents.NavigateUp)
            }
            is ChangeTodoItemActions.UpdateText -> _uiState.update {
                uiState.value.copy(text = action.text)
            }
            is ChangeTodoItemActions.UpdateDeadlineSet -> _uiState.update {
                uiState.value.copy(isDeadlineSet = action.isDeadlineSet)
            }
            is ChangeTodoItemActions.UpdateImportance -> _uiState.update {
                uiState.value.copy(importance = action.importance)
            }
            is ChangeTodoItemActions.UpdateDeadline -> _uiState.update {
                uiState.value.copy(deadline = unixToDate(action.deadline))
            }
        }
    }

    private fun saveTodoItem() {
        if (uiState.value.text.isBlank()) return

        val todoItem = if (isNewItem) {
            TodoItem(
                id = id,
                text = uiState.value.text,
                importance = uiState.value.importance,
                deadline = if (uiState.value.isDeadlineSet) dateToUnix(uiState.value.deadline) else null
            )
        } else {
            oldTodoItem!!.copy(
                text = uiState.value.text,
                importance = uiState.value.importance,
                deadline = if (uiState.value.isDeadlineSet) dateToUnix(uiState.value.deadline) else null,
                modificationDate = dateToUnix(Date())
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (isNewItem) repository.addTodoItem(todoItem)
            else repository.updateTodoItem(todoItem)
            _uiEvent.send(ChangeTodoItemEvents.SaveTodoItem)
        }
    }

    private fun removeTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isNewItem)
                oldTodoItem?.let { repository.removeTodoItem(id) }
            _uiEvent.send(ChangeTodoItemEvents.NavigateUp)
        }
    }
}