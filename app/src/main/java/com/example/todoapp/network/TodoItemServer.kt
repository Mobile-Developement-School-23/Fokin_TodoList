package com.example.todoapp.network

import com.example.todoapp.TodoItem
import com.example.todoapp.utils.stringToImportance
import com.google.gson.annotations.SerializedName

data class TodoItemServer(
    @SerializedName("id") val id: String?,
    @SerializedName("text") val text: String? = null,
    @SerializedName("importance") val importance: String? = null,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean? = null,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") val created_at: Long? = null,
    @SerializedName("changed_at") val changed_at: Long? = null,
    @SerializedName("last_updated_by") val last_updated_by: String? = null
) {

    fun toTodoItem(): TodoItem {
        return TodoItem(
            id = id!!,
            text = text!!,
            importance = stringToImportance(importance!!),
            deadline = deadline,
            isDone = done!!,
            creationDate = created_at!!,
            modificationDate = changed_at!!
        )
    }
}