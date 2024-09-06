package com.example.traveljournal.data

import android.net.Uri
import android.provider.BaseColumns

object DataContract {
    const val DATABASE_NAME = "journal.db"
    const val DATABASE_VERSION = 1


    object JournalEntry : BaseColumns {
        const val TABLE_NAME = "journal_entries"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"

        val CONTENT_URI: Uri = Uri.parse("content://com.example.traveljournal/journal_entries")
    }
}
