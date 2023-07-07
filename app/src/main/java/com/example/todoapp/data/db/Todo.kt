package com.example.todoapp.data.db

data class Todo(
    val id: String,
    var importanceId: Int,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
) {

    fun toTodoDbEntity(): TodoEntity = TodoEntity(
        id = id,
        importanceId = importanceId,
        text = text,
        deadline = deadline,
        done = done,
        createdAt = createdAt,
        changedAt = changedAt
    )
}
