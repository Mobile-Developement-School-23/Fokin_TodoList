package com.example.todoapp.utils

import android.content.res.Resources
import android.util.TypedValue
import com.example.todoapp.data.network.TodoItemFromServer
import com.example.todoapp.data.Importance
import com.example.todoapp.data.TodoItem
import com.example.todoapp.data.db.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val URL = "https://beta.mrdekk.ru/todobackend/"
const val TOKEN = "Bearer trinchera"
const val CONNECT_TIMEOUT = 120L
const val READ_TIMEOUT = 120L
const val WRITE_TIMEOUT = 90L
const val REPEAT_INTERVAL = 8L
fun generateRandomItemId(): String =
    SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())

fun formatDate(date: Long): String =
    SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(Date(date * 1000L))

fun stringToImportance(importance: String): Importance {
    return when (importance) {
        "important" -> Importance.IMPORTANT
        "basic" -> Importance.BASIC
        else -> Importance.LOW
    }
}

fun importanceToString(importance: Importance): String {
    return when (importance) {
        Importance.IMPORTANT -> "important"
        Importance.BASIC -> "basic"
        else -> "low"
    }
}

fun dateToUnix(date: Date) = date.time / 1000

fun getImportanceId(importance: Importance): Int {
    return when (importance) {
        Importance.IMPORTANT -> 3
        Importance.BASIC -> 2
        else -> 1
    }
}

fun toTodoItemServer(todoItem: TodoItem): TodoItemFromServer {
    return TodoItemFromServer(
        id = todoItem.id,
        text = todoItem.text,
        importance = importanceToString(todoItem.importance),
        deadline = todoItem.deadline,
        done = todoItem.isDone,
        created_at = todoItem.creationDate,
        changed_at = todoItem.modificationDate,
        last_updated_by = "cf1"
    )
}

fun createTodo(todoItem: TodoItem): Todo {
    return Todo(
        id = todoItem.id,
        text = todoItem.text,
        importanceId = getImportanceId(todoItem.importance),
        deadline = todoItem.deadline,
        done = todoItem.isDone,
        createdAt = todoItem.creationDate,
        changedAt = todoItem.modificationDate
    )
}

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )