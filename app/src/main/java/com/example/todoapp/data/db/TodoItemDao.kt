package com.example.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TodoItemDao {

    @Insert(entity = TodoEntity::class)
    fun insertNewTodoItemData(todo: TodoEntity)

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt FROM todo\n" +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id")
    fun getAllTodoData(): List<TodoItemInfo>

    @Query("DELETE FROM todo WHERE id = :todoId")
    fun deleteTodoDataById(todoId: String)

    @Query("SELECT todo.id, importance_name, text, deadline, done, createdAt, changedAt FROM todo\n" +
            "INNER JOIN importance_levels ON todo.importance_id = importance_levels.id\n" +
            "WHERE todo.id = :todoId")
    fun getTodoDataById(todoId: String): TodoItemInfo?

    @Update(entity = TodoEntity::class)
    fun updateTodoData(todo: TodoEntity)

    @Transaction
    fun replaceAllTodoItems(todoItems: List<TodoEntity>) {
        deleteAllTodoItems()
        todoItems.forEach {
            insertNewTodoItemData(it)
        }
    }

    @Query("DELETE FROM todo")
    fun deleteAllTodoItems()
}