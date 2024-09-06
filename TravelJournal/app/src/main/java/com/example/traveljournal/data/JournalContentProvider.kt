package com.example.traveljournal.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class JournalContentProvider : ContentProvider() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return db.query(
            DatabaseHelper.TABLE_ENTRIES,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = db.insert(DatabaseHelper.TABLE_ENTRIES, null, values)
        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DatabaseHelper.TABLE_ENTRIES, selection, selectionArgs)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        return db.update(DatabaseHelper.TABLE_ENTRIES, values, selection, selectionArgs)
    }
}
