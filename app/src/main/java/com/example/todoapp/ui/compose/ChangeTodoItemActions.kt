package com.example.todoapp.ui.compose

import com.example.todoapp.data.Importance

sealed class ChangeTodoItemActions{
    data class UpdateText(val text: String) : ChangeTodoItemActions()
    data class UpdateImportance(val importance: Importance) : ChangeTodoItemActions()
    data class UpdateDeadline(val deadline: Long?) : ChangeTodoItemActions()

    object Close : ChangeTodoItemActions()
    object Save : ChangeTodoItemActions()
    object Delete : ChangeTodoItemActions()
}
