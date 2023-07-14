package com.example.todoapp.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.Importance
import com.example.todoapp.data.TodoItem
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.fillMaxWidth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Date

@Preview
@Composable
fun PreviewChangeTodoItemScreenLightTheme() {
    AppTheme(darkTheme = false) {
        ChangeTodoItemScreen(
            uiState = ChangeTodoItemState(
                text = "Hello",
                importance = Importance.IMPORTANT,
                deadline = Date(),
                isDeadlineSet = true,
                isNewItem = false
            ),
            uiEvent = Channel<ChangeTodoItemEvents>().receiveAsFlow(),
            uiAction = {},
            onNavigateUp = {},
            onSave = {}
        )
    }
}

@Preview
@Composable
fun PreviewChangeTodoItemScreenDarkTheme() {
    AppTheme(darkTheme = true) {
        ChangeTodoItemScreen(
            uiState = ChangeTodoItemState(
                text = "Hello",
                importance = Importance.IMPORTANT,
                deadline = Date(),
                isDeadlineSet = true,
                isNewItem = false
            ),
            uiEvent = Channel<ChangeTodoItemEvents>().receiveAsFlow(),
            uiAction = {},
            onNavigateUp = {},
            onSave = {}
        )
    }
}

@Composable
fun ChangeTodoItemScreen(
    uiState: ChangeTodoItemState,
    uiEvent: Flow<ChangeTodoItemEvents>,
    uiAction: (ChangeTodoItemActions) -> Unit,
    onNavigateUp: () -> Unit,
    onSave: () -> Unit
) {
    ChangeTodoItemActionHandler(
        uiEvent = uiEvent,
        onNavigateUp = onNavigateUp,
        onSave = onSave
    )

    Scaffold(
        topBar = {
            ChangeTodoTopBarComponent(text = uiState.text, onAction = uiAction)
        },
        containerColor = AppTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ChangeTodoTextComponent(
                    text = uiState.text,
                    onAction = uiAction
                )
                ChangeTodoImportanceComponent(importance = uiState.importance, uiAction = uiAction)
                ChangeTodoDivider(padding = PaddingValues(horizontal = 16.dp))
                ChangeTodoItemDeadlineComponent(
                    deadline = uiState.deadline,
                    isDateVisible = uiState.isDeadlineSet,
                    uiAction = uiAction
                )
                ChangeTodoDivider(padding = PaddingValues(top = 16.dp, bottom = 8.dp))
                ChangeTodoDeleteComponent(enabled = uiState.isDeleteEnabled, onAction = uiAction)
            }
        }
    }
}

@Composable
fun ChangeTodoDivider(padding: PaddingValues) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        thickness = 0.5.dp,
        color = AppTheme.colors.supportSeparator
    )
}