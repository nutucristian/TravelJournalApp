package com.example.traveljournal.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.traveljournal.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTextName: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("TravelJournalPrefs", MODE_PRIVATE)

        editTextName = findViewById(R.id.editTextName)
        saveButton = findViewById(R.id.saveButton)

        val name = sharedPreferences.getString("userName", "")
        editTextName.setText(name)

        saveButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("userName", editTextName.text.toString())
            editor.apply()
            finish()
        }
    }
}
