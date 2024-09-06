package com.example.traveljournal.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "traveljournal.db"
        private const val DATABASE_VERSION = 3
        const val TABLE_ENTRIES = "entries"
        const val COLUMN_ID = "id"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_ENTRIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_IMAGE + " BLOB" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_ENTRIES ADD COLUMN $COLUMN_LOCATION TEXT")
        }
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE $TABLE_ENTRIES ADD COLUMN $COLUMN_IMAGE BLOB")
        }
    }

    fun getEntriesByDate(timestamp: Long): List<JournalEntry> {
        val entries = mutableListOf<JournalEntry>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ENTRIES,
            arrayOf(COLUMN_ID, COLUMN_CONTENT, COLUMN_TIMESTAMP, COLUMN_LOCATION, COLUMN_IMAGE),
            "$COLUMN_TIMESTAMP >= ? AND $COLUMN_TIMESTAMP < ?",
            arrayOf(timestamp.toString(), (timestamp + 86400000).toString()), // 86400000 ms in a day
            null, null, "$COLUMN_TIMESTAMP ASC"
        )

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val entry = JournalEntry(
                        it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        it.getBlob(it.getColumnIndexOrThrow(COLUMN_IMAGE))
                    )
                    entries.add(entry)
                } while (it.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return entries
    }

    fun addEntry(entry: JournalEntry): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, entry.content)
            put(COLUMN_TIMESTAMP, entry.timestamp)
            put(COLUMN_LOCATION, entry.location)
            put(COLUMN_IMAGE, entry.image)
        }
        val id = db.insert(TABLE_ENTRIES, null, values)
        db.close()
        return id
    }

    fun updateEntry(entry: JournalEntry) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, entry.content)
            put(COLUMN_TIMESTAMP, entry.timestamp)
            put(COLUMN_LOCATION, entry.location)
            put(COLUMN_IMAGE, entry.image)
        }
        db.update(TABLE_ENTRIES, values, "$COLUMN_ID=?", arrayOf(entry.id.toString()))
        db.close()
    }

    fun getAllEntries(): List<JournalEntry> {
        val entries = mutableListOf<JournalEntry>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ENTRIES,
            arrayOf(COLUMN_ID, COLUMN_CONTENT, COLUMN_TIMESTAMP, COLUMN_LOCATION, COLUMN_IMAGE),
            null, null, null, null, "$COLUMN_TIMESTAMP DESC"
        )

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val entry = JournalEntry(
                        it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        it.getLong(it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                        it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        it.getBlob(it.getColumnIndexOrThrow(COLUMN_IMAGE))
                    )
                    entries.add(entry)
                } while (it.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return entries
    }

    fun deleteEntry(entryId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_ENTRIES, "$COLUMN_ID=?", arrayOf(entryId.toString()))
        db.close()
    }

    fun getEntryById(id: Long): JournalEntry? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ENTRIES,
            arrayOf(COLUMN_ID, COLUMN_CONTENT, COLUMN_TIMESTAMP, COLUMN_LOCATION, COLUMN_IMAGE),
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null, null, null
        )

        var entry: JournalEntry? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                entry = JournalEntry(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                )
            }
            cursor.close()
        }
        db.close()
        return entry
    }
}
