package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.ui.ChangeTodoItemFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class, NotificationModule::class])
interface ChangeTodoItemComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @FragmentQualifier context: Context): ChangeTodoItemComponent
    }
    fun inject(fragment: ChangeTodoItemFragment)
}