package com.example.todoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.db.ImportanceEntity
import com.example.todoapp.db.RevisionDao
import com.example.todoapp.db.RevisionEntity
import com.example.todoapp.db.TodoEntity
import com.example.todoapp.db.TodoItemDao

@Database(
    version = 4,
    entities = [
        ImportanceEntity::class,
        TodoEntity::class,
        RevisionEntity::class
    ]
)
abstract class TodoListDB : RoomDatabase() {

    abstract fun getTodoItemDao(): TodoItemDao

    abstract fun getRevisionDao(): RevisionDao
}