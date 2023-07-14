package com.example.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.data.MyRepository
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.db.RevisionDao
import com.example.todoapp.data.db.TodoItemDao
import com.example.todoapp.data.db.TodoListDB

@Module
interface DataModule {

    @AppScope
    @Binds
    fun provideRepository(repository: MyRepository): TodoItemsRepository

    companion object {
        @AppScope
        @Provides
        fun provideTodoItemDao(database: TodoListDB): TodoItemDao {
            return database.getTodoItemDao()
        }

        @AppScope
        @Provides
        fun provideRevisionDao(database: TodoListDB): RevisionDao {
            return database.getRevisionDao()
        }

        @AppScope
        @Provides
        fun provideDatabase(context: Context): TodoListDB {
            return TodoListDB.getDatabaseInstance(context)
        }

        @AppScope
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        }
    }
}