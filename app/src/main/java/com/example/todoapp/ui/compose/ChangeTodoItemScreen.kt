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

@Preview
@Composable
fun PreviewChangeTodoItemScreenLightTheme() {
    AppTheme(darkTheme = false) {
        ChangeTodoItemScreen(
            TodoItem(
                id = "322",
                text = "Парампампап",
                importance = Importance.IMPORTANT,
                deadline = System.currentTimeMillis()
            ),
            isEditing = true,
            onAction = {})
    }
}

@Preview
@Composable
fun PreviewChangeTodoItemScreenDarkTheme() {
    AppTheme(darkTheme = true) {
        ChangeTodoItemScreen(
            TodoItem(
                id = "322",
                text = "Парампампап",
                importance = Importance.IMPORTANT,
                deadline = System.currentTimeMillis()
            ),
            isEditing = true,
            onAction = {})
    }
}

@Composable
fun ChangeTodoItemScreen(
    todoItem: TodoItem,
    isEditing: Boolean,
    onAction: (ChangeTodoItemActions) -> Unit
) {
    val text = todoItem.text
    val importance = todoItem.importance
    val deadline = todoItem.deadline

    Scaffold(
        topBar = { ChangeTodoTopBarComponent(text = text, onAction = onAction) },
        containerColor = AppTheme.colors.backPrimary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ChangeTodoTextComponent(text = text, onAction = onAction)
                ChangeTodoImportanceComponent(importance = importance, onAction = onAction)
                ChangeTodoDivider(PaddingValues(horizontal = 16.dp))
                ChangeTodoDeadlineComponent(deadline = deadline, onAction = onAction)
                ChangeTodoDivider(PaddingValues(top = 24.dp, bottom = 8.dp))
                ChangeTodoDeleteComponent(
                    enabled = isEditing || todoItem.text.isNotEmpty(),
                    onAction = onAction
                )
                Spacer(Modifier.size(33.dp))
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