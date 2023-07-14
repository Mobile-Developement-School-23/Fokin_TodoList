package com.example.todoapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.utils.CHANNEL_ID
import com.example.todoapp.utils.INTENT_ID_IMPORTANCE_KEY
import com.example.todoapp.utils.INTENT_ID_KEY
import com.example.todoapp.utils.INTENT_ID_TITLE_KEY

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(INTENT_ID_KEY, 0)
        val title = intent.getStringExtra(INTENT_ID_TITLE_KEY)
        val importance = context.getString(R.string.importance) +
                context.getString(intent.getIntExtra(INTENT_ID_IMPORTANCE_KEY, 0))
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(importance)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }
}