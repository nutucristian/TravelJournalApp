package com.example.traveljournal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class NotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Notification Service Started", Toast.LENGTH_SHORT).show()

        val notificationHelper = NotificationHelper(this)
        notificationHelper.sendNotification("Travel Journal", "Service Started")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Notification Service Destroyed", Toast.LENGTH_SHORT).show()
    }
}
