package com.example.todoapp.data

import com.example.todoapp.data.Importance
import com.example.todoapp.utils.dateToUnix
import java.util.Date

data class TodoItem(
    val id: String,
    var text: String = "",
    var importance: Importance = Importance.BASIC,
    var deadline: Long? = null,
    var isDone: Boolean = false,
    val creationDate: Long = dateToUnix(Date()),
    var modificationDate: Long = dateToUnix(Date())
)