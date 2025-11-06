package com.example.futurescript.data.database.entities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Letter @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val deliverAtEpochSec: Long,
    val createdAtEpochSec: Long = Instant.now().epochSecond,
    val delivered: Boolean = false
)