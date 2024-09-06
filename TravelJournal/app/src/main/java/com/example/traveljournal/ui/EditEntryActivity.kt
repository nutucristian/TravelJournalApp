package com.example.traveljournal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.traveljournal.R
import com.example.traveljournal.data.DatabaseHelper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.traveljournal.data.JournalEntry
import android.graphics.BitmapFactory


class EditEntryActivity : AppCompatActivity() {
    private lateinit var editTextContent: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var imageViewEntry: ImageView
    private lateinit var dbHelper: DatabaseHelper
    private var entryId: Long = -1
    private var entryImage: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)

        editTextContent = findViewById(R.id.editTextContent)
        editTextLocation = findViewById(R.id.editTextLocation)
        imageViewEntry = findViewById(R.id.imageViewEntry)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        dbHelper = DatabaseHelper(this)

        entryId = intent.getLongExtra("entryId", -1)
        if (entryId != -1L) {
            val entry = dbHelper.getEntryById(entryId)
            entry?.let {
                editTextContent.setText(it.content)
                editTextLocation.setText(it.location)
                entryImage = it.image
                if (entryImage != null) {
                    imageViewEntry.setImageBitmap(BitmapFactory.decodeByteArray(entryImage, 0, entryImage!!.size))
                }
            }
        }

        saveButton.setOnClickListener {
            val content = editTextContent.text.toString()
            val location = editTextLocation.text.toString()
            if (entryId != -1L) {
                val updatedEntry = JournalEntry(entryId, content, System.currentTimeMillis(), location, entryImage)
                dbHelper.updateEntry(updatedEntry)
                setResult(RESULT_OK)
                finish()
            }
        }

        deleteButton.setOnClickListener {
            if (entryId != -1L) {
                dbHelper.deleteEntry(entryId)
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}
