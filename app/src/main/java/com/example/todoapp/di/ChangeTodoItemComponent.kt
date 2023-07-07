package com.example.todoapp.di

import com.example.todoapp.ui.ChangeTodoItemFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface ChangeTodoItemComponent {
    fun inject(fragment: ChangeTodoItemFragment)
}