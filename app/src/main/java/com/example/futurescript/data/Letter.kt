package com.example.futurescript.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Letter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val deliverAtEpochSec: Long,
    val createdAtEpochSec: Long = Instant.now().epochSecond,
    val delivered: Boolean = false
)
