package com.example.traveljournal.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.traveljournal.R

class CountryListActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val countryList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries_visited)

        listView = findViewById(R.id.country_list_view)
        adapter = CountryListAdapter()
        listView.adapter = adapter

        findViewById<Button>(R.id.add_country_button).setOnClickListener {
            showAddCountryDialog()
        }

        loadCountryList()
        updateHeader()
    }

    private fun showAddCountryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Country")

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_country, null)
        builder.setView(view)

        val input = view.findViewById<EditText>(R.id.editTextCountry)

        builder.setPositiveButton("Add") { _, _ ->
            val country = input.text.toString()
            if (country.isNotEmpty()) {
                countryList.add(country)
                adapter.notifyDataSetChanged()
                updateHeader()
                saveCountryList()
                Toast.makeText(this, "Country added", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showEditCountryDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Country")

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_country, null)
        builder.setView(view)

        val input = view.findViewById<EditText>(R.id.editTextCountry)
        input.setText(countryList[position])

        builder.setPositiveButton("Update") { _, _ ->
            val country = input.text.toString()
            if (country.isNotEmpty()) {
                countryList[position] = country
                adapter.notifyDataSetChanged()
                saveCountryList()
                Toast.makeText(this, "Country updated", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNeutralButton("Delete") { _, _ ->
            showDeleteCountryDialog(position)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showDeleteCountryDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Country")
        builder.setMessage("Are you sure you want to delete this country?")

        builder.setPositiveButton("Delete") { _, _ ->
            countryList.removeAt(position)
            adapter.notifyDataSetChanged()
            updateHeader()
            saveCountryList()
            Toast.makeText(this, "Country deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun updateHeader() {
        val header = findViewById<TextView>(R.id.text_view_header)
        header.text = "Countries visited (${countryList.size})"
    }

    private fun saveCountryList() {
        val sharedPreferences = getSharedPreferences("travel_journal_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("country_list", countryList.toSet())
        editor.apply()
    }

    private fun loadCountryList() {
        val sharedPreferences = getSharedPreferences("travel_journal_prefs", MODE_PRIVATE)
        val savedCountrySet = sharedPreferences.getStringSet("country_list", setOf("United States", "India"))
        countryList.clear()
        countryList.addAll(savedCountrySet!!)
        adapter.notifyDataSetChanged()
    }

    private inner class CountryListAdapter : ArrayAdapter<String>(this@CountryListActivity, R.layout.country_list_item, countryList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.country_list_item, parent, false)
            val countryName = view.findViewById<TextView>(R.id.country_name)
            val editButton = view.findViewById<TextView>(R.id.edit_button)

            countryName.text = countryList[position]
            editButton.setOnClickListener {
                showEditCountryDialog(position)
            }

            return view
        }
    }
}
