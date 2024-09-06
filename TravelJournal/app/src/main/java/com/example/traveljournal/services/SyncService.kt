package com.example.traveljournal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        // Return the communication channel to the service.
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If we get killed, after returning from here, restart
        return START_STICKY
    }
}
