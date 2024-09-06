package com.example.traveljournal.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.traveljournal.R
import com.example.traveljournal.data.DatabaseHelper
import com.example.traveljournal.data.JournalEntry
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.ByteArrayOutputStream

class AddEntryActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var entryContent: EditText
    private lateinit var entryLocation: EditText
    private lateinit var imageViewEntry: ImageView
    private lateinit var addImageButton: Button
    private lateinit var saveButton: Button
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        dbHelper = DatabaseHelper(this)
        entryContent = findViewById(R.id.editTextContent)
        entryLocation = findViewById(R.id.editTextLocation)
        imageViewEntry = findViewById(R.id.imageViewEntry)
        addImageButton = findViewById(R.id.buttonAddImage)
        saveButton = findViewById(R.id.saveButton)

        entryLocation.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        }

        addImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            } else {
                openCamera()
            }
        }

        saveButton.setOnClickListener {
            val content = entryContent.text.toString()
            val location = entryLocation.text.toString()

            if (content.isNotEmpty() && location.isNotEmpty()) {
                val newEntry = JournalEntry(content = content, timestamp = System.currentTimeMillis(), location = location, image = imageBitmapToByteArray(imageBitmap))
                dbHelper.addEntry(newEntry)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            imageViewEntry.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            entryLocation.setText(place.address)
        }
    }

    private fun imageBitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CODE_AUTOCOMPLETE = 2
    }
}
