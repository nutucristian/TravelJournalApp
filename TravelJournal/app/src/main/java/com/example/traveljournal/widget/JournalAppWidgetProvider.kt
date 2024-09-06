package com.example.traveljournal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.traveljournal.R
import com.example.traveljournal.ui.MainActivity
import com.example.traveljournal.ui.AddEntryActivity

class JournalAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE)

            val addEntryIntent = Intent(context, AddEntryActivity::class.java)
            val addEntryPendingIntent = PendingIntent.getActivity(context, 0, addEntryIntent, PendingIntent.FLAG_IMMUTABLE)

            val views = RemoteViews(context.packageName, R.layout.journal_widget).apply {
                setOnClickPendingIntent(R.id.widget_button_open_app, openAppPendingIntent)
                setOnClickPendingIntent(R.id.widget_button_add_entry, addEntryPendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
