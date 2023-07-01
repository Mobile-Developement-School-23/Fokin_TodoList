package com.example.todoapp

import android.content.Context
import androidx.room.Room
import com.example.todoapp.db.TodoListDB

object Di {
    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val todoListDB: TodoListDB by lazy {
        Room.databaseBuilder(applicationContext, TodoListDB::class.java, "database.db")
            .createFromAsset("todo_database.db")
            .build()
    }

    val basedRepository: BasedRepository by lazy {
        BasedRepository(
            todoListDB.getTodoItemDao(),
            todoListDB.getRevisionDao()
        )
    }
}