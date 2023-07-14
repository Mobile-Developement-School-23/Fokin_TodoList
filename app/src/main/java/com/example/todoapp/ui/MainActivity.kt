package com.example.todoapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.todoapp.R
import com.example.todoapp.utils.SHAR_PREF_THEME

class MainActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        when (sharedPref.getInt(SHAR_PREF_THEME, 2)){
            0 -> setLightTheme(null)
            1 -> setDarkTheme(null)
            else -> setSystemTheme(null)
        }
    }

    fun setLightTheme(view: View?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) return
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sharedPref.edit().putInt(SHAR_PREF_THEME, 0).apply()
        ActivityCompat.recreate(this)
    }

    fun setDarkTheme(view: View?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) return
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        sharedPref.edit().putInt(SHAR_PREF_THEME, 1).apply()
        ActivityCompat.recreate(this)
    }

    fun setSystemTheme(view: View?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) return
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        sharedPref.edit().putInt(SHAR_PREF_THEME, 2).apply()
        ActivityCompat.recreate(this)
    }
}