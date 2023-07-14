package com.example.todoapp.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.utils.SHAR_PREF_THEME
import com.example.todoapp.utils.saveNotificationsPermission
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPref: SharedPreferences
    private lateinit var pLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        (application as App)
            .appComponent
            .inject(this)

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        when (sharedPref.getInt(SHAR_PREF_THEME, 2)){
            0 -> setLightTheme(null)
            1 -> setDarkTheme(null)
            else -> setSystemTheme(null)
        }

        registerPermissionListener()
        checkNotificationPermission()
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

    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> { saveNotificationsPermission(sharedPref, true) }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {}
            else -> {
                pLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                saveNotificationsPermission(sharedPref, true)
            } else {
                saveNotificationsPermission(sharedPref, false)
                Toast.makeText(this, R.string.no_notifications, Toast.LENGTH_LONG).show()
            }
        }
    }
}