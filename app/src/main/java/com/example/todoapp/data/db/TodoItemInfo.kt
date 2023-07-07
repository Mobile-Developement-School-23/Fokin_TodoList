package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import com.example.todoapp.data.TodoItem
import com.example.todoapp.utils.stringToImportance

data class TodoItemInfo(
    val id: String,
    @ColumnInfo(name = "importance_name") var importance: String,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
) {

    fun toTodoItem(): TodoItem = TodoItem(
        id = id,
        text = text,
        importance = stringToImportance(importance),
        deadline = deadline,
        isDone = done,
        creationDate = createdAt,
        modificationDate = changedAt
    )
}
