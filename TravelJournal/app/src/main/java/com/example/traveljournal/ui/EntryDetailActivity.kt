package com.example.traveljournal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.example.traveljournal.R
import com.example.traveljournal.data.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date


class EntryDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_detail)

        dbHelper = DatabaseHelper(this)

        val entryId = intent.getLongExtra("entryId", -1)
        val entry = dbHelper.getEntryById(entryId)




        
    }
}
