package com.example.traveljournal.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveljournal.R
import com.example.traveljournal.data.DatabaseHelper
import com.example.traveljournal.data.JournalEntry
import com.example.traveljournal.services.LocationService
import com.example.traveljournal.services.NotificationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var addEntryButton: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var calendarView: CalendarView
    private lateinit var entriesHeader: TextView
    private lateinit var recyclerViewEntries: RecyclerView
    private lateinit var entryAdapter: EntryAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        checkAndRequestNotificationPermission()

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Start the service
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        calendarView = findViewById(R.id.calendarView)
        entriesHeader = findViewById(R.id.entriesHeader)
        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        addEntryButton = findViewById(R.id.fab)

        sharedPreferences = getSharedPreferences("TravelJournalPrefs", MODE_PRIVATE)
        updateToolbarTitle()

        dbHelper = DatabaseHelper(this)

        entryAdapter = EntryAdapter(
            listOf(),
            { entry ->
                // Handle item click
                val intent = Intent(this, EntryDetailActivity::class.java)
                intent.putExtra("entryId", entry.id)
                startActivity(intent)
            },
            { entry ->
                // Handle edit click
                val intent = Intent(this, EditEntryActivity::class.java)
                intent.putExtra("entryId", entry.id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_ENTRY)
            },
            { entry ->
                // Handle delete click
                dbHelper.deleteEntry(entry.id)
                updateEntries(selectedDate)
            }
        )

        recyclerViewEntries.layoutManager = LinearLayoutManager(this)
        recyclerViewEntries.adapter = entryAdapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        // Handle date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            updateEntries(selectedDate)
        }

        addEntryButton.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_ENTRY)
        }

        // Load entries for the current date on startup
        selectedDate = Date()
        updateEntries(selectedDate)
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle()
        updateEntries(selectedDate)
    }

    private fun updateToolbarTitle() {
        val userName = sharedPreferences.getString("userName", "")
        if (userName != null && userName.isNotEmpty()) {
            toolbar.title = "$userName's Travel Journal"
        } else {
            toolbar.title = getString(R.string.app_name)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        } else {
            getLocation()
            startLocationService()
        }
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // Utilizează locația
                }
        } else {
            // Permisiunea nu a fost acordată
        }
    }

    private val REQUEST_CODE_POST_NOTIFICATIONS = 1001

    private fun checkAndRequestNotificationPermission() {
        if (SDK_INT >= VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permisiunea a fost acordată, trimite notificarea
                sendNotification("Title", "Message")
            } else {
                // Permisiunea nu a fost acordată
            }
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.sendNotification(title, message)
    }

    private fun updateEntries(date: Date) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        entriesHeader.text = "Entries for $formattedDate"

        val entries = dbHelper.getEntriesByDate(date.time)
        entryAdapter.updateEntries(entries)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_CODE_ADD_ENTRY || requestCode == REQUEST_CODE_EDIT_ENTRY) && resultCode == RESULT_OK) {
            updateEntries(selectedDate)
        }
        if (requestCode == REQUEST_CODE_SETTINGS) {
            updateToolbarTitle()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add_entry -> {
                val intent = Intent(this, AddEntryActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD_ENTRY)
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_SETTINGS)
            }
            R.id.nav_view_entries -> {
                val intent = Intent(this, ViewEntryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_countries_visited -> {
                val intent = Intent(this, CountryListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_about_us -> {
                val intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    class EntryAdapter(
        private var entries: List<JournalEntry>,
        private val onItemClick: (JournalEntry) -> Unit,
        private val onEditClick: (JournalEntry) -> Unit,
        private val onDeleteClick: (JournalEntry) -> Unit
    ) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

        inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val entryContent: TextView = view.findViewById(R.id.entry_content)
            val entryLocation: TextView = view.findViewById(R.id.entry_location)
            val entryDate: TextView = view.findViewById(R.id.entry_date)
            val entryImage: ImageView = view.findViewById(R.id.entry_image)
            val editButton: ImageButton = view.findViewById(R.id.edit_button)
            val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry, parent, false)
            return EntryViewHolder(view)
        }

        override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
            val entry = entries[position]
            holder.entryContent.text = entry.content
            holder.entryLocation.text = entry.location
            holder.entryDate.text = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(Date(entry.timestamp))

            if (entry.image != null) {
                holder.entryImage.setImageBitmap(BitmapFactory.decodeByteArray(entry.image, 0, entry.image.size))
            }

            holder.editButton.setOnClickListener { onEditClick(entry) }
            holder.deleteButton.setOnClickListener { onDeleteClick(entry) }

            holder.itemView.setOnClickListener { onItemClick(entry) }
        }

        override fun getItemCount(): Int {
            return entries.size
        }

        fun updateEntries(newEntries: List<JournalEntry>) {
            entries = newEntries
            notifyDataSetChanged()
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_ENTRY = 1
        const val REQUEST_CODE_EDIT_ENTRY = 2
        const val REQUEST_CODE_LOCATION_PERMISSION = 3
        const val REQUEST_CODE_SETTINGS = 4
    }
}
