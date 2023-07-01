package com.example.todoapp

import android.content.Context
import com.example.todoapp.R

enum class Importance {
    LOW {
        override fun getLocalizedName(context: Context): String {
            return context.getString(R.string.importance_low)
        }
    },
    BASIC {
        override fun getLocalizedName(context: Context): String {
            return context.getString(R.string.importance_normal)
        }
    },
    IMPORTANT {
        override fun getLocalizedName(context: Context): String {
            return context.getString(R.string.importance_urgent)
        }
    };

    abstract fun getLocalizedName(context: Context): String
}