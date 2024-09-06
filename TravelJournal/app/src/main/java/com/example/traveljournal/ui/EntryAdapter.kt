// EntryAdapter.kt
package com.example.traveljournal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traveljournal.R
import java.text.SimpleDateFormat
import java.util.*
import com.example.traveljournal.data.JournalEntry

class EntryAdapter(
    private var entries: List<JournalEntry>,
    private val onItemClick: (JournalEntry) -> Unit,
    private val onEditClick: (JournalEntry) -> Unit,
    private val onDeleteClick: (JournalEntry) -> Unit
) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
        val content: TextView = itemView.findViewById(R.id.content)
        val editIcon: ImageView = itemView.findViewById(R.id.edit_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_entry_item, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.timestamp.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(entry.timestamp))
        holder.content.text = entry.content

        holder.itemView.setOnClickListener {
            onItemClick(entry)
        }

        holder.editIcon.setOnClickListener {
            onEditClick(entry)
        }


    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateEntries(newEntries: List<JournalEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
