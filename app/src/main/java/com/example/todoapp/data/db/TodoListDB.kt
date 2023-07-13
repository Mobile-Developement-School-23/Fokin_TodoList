package com.example.todoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
