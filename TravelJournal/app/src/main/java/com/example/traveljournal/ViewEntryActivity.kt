package com.example.traveljournal.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveljournal.R
import com.example.traveljournal.data.DatabaseHelper
import com.example.traveljournal.data.JournalEntry

class ViewEntryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var entryAdapter: EntryAdapter
    private lateinit var recyclerViewEntries: RecyclerView
    private lateinit var entryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_entry)

        dbHelper = DatabaseHelper(this)

        entryAdapter = EntryAdapter(
            listOf(),
            { entry: JournalEntry ->
                // Handle item click (if needed)
            },
            { entry: JournalEntry ->
                // Handle edit click
                val intent = Intent(this, EditEntryActivity::class.java)
                intent.putExtra("entryId", entry.id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_ENTRY)
            },
            { entry: JournalEntry ->
                // Handle delete click
                dbHelper.deleteEntry(entry.id)
                updateEntries()
            }
        )


        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        entryTextView = findViewById(R.id.entryTextView)

        recyclerViewEntries.layoutManager = LinearLayoutManager(this)
        recyclerViewEntries.adapter = entryAdapter

        updateEntries()
    }

    private fun updateEntries() {
        val entries = dbHelper.getAllEntries()
        if (entries.isEmpty()) {
            entryTextView.visibility = View.VISIBLE
        } else {
            entryTextView.visibility = View.GONE
        }
        entryAdapter.updateEntries(entries)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_ENTRY && resultCode == RESULT_OK) {
            updateEntries()
        }
    }

    companion object {
        const val REQUEST_CODE_EDIT_ENTRY = 2
    }
}
