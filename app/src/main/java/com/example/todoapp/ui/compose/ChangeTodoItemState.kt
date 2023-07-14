package com.example.todoapp.ui.compose

import com.example.todoapp.data.Importance
import java.util.Date

data class ChangeTodoItemState(
    val text: String = "",
    val importance: Importance = Importance.BASIC,
    val deadline: Date = Date(),
    val isDeadlineSet: Boolean = false,
    val isNewItem: Boolean = true
) {
    val isDeleteEnabled: Boolean
        get() = text.isNotBlank() || !isNewItem
}
