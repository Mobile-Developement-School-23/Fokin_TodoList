package com.example.todoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.db.ImportanceEntity
import com.example.todoapp.data.db.RevisionDao
import com.example.todoapp.data.db.RevisionEntity
import com.example.todoapp.data.db.TodoEntity
import com.example.todoapp.data.db.TodoItemDao
import androidx.room.Room
import android.content.Context

@Database(
    version = 1,
    entities = [
        ImportanceEntity::class,
        TodoEntity::class,
        RevisionEntity::class
    ]
)
abstract class TodoListDB : RoomDatabase() {

    abstract fun getTodoItemDao(): TodoItemDao

    abstract fun getRevisionDao(): RevisionDao

    companion object {
        private var database: TodoListDB? = null

        fun getDatabaseInstance(context: Context): TodoListDB {
            return if (database == null) {
                synchronized(this) {
                    Room.databaseBuilder(context, TodoListDB::class.java, "database.db")
                        .createFromAsset("todo_database.db")
                        .build()
                }
            } else {
                database!!
            }
        }
    }
}