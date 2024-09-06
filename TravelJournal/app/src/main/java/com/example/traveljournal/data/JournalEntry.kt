package com.example.traveljournal.data

data class JournalEntry(
    val id: Long = 0, // Default value for new entries
    val content: String,
    val timestamp: Long,
    val location: String,
    val image: ByteArray?
)
