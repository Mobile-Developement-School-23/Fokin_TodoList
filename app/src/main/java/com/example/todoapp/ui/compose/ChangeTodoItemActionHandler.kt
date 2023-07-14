package com.example.todoapp.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun ChangeTodoItemActionHandler(
    uiEvent: Flow<ChangeTodoItemEvents>,
    onNavigateUp: () -> Unit,
    onSave: () -> Unit
) {
    LaunchedEffect(Unit) {
        uiEvent.collect {
            when(it) {
                ChangeTodoItemEvents.NavigateUp -> onNavigateUp()
                ChangeTodoItemEvents.SaveTodoItem -> onSave()
            }
        }
    }
}