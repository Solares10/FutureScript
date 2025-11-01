package com.example.futurescript.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LetterDao {
    @Insert suspend fun insert(letter: Letter): Long
    @Query("SELECT * FROM Letter ORDER BY deliverAtEpochSec ASC")
    fun watchAll(): Flow<List<Letter>>
    @Query("UPDATE Letter SET delivered = 1 WHERE id = :id")
    suspend fun markDelivered(id: Long)
}
