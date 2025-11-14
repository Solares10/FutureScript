package com.example.futurescript.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// ✅ Defines the "Letter" table for your Room database.
@Entity(tableName = "Letter")
data class Letter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val message: String,
    val createdAtEpochSec: Long,
    val deliverAtEpochSec: Long,
    val delivered: Boolean = false
)
