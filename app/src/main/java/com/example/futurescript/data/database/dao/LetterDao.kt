package com.example.futurescript.data.database.dao

import androidx.room.*
import com.example.futurescript.data.database.entities.Letter
import kotlinx.coroutines.flow.Flow


@Dao
interface LetterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(letter: Letter): Long

    @Query("SELECT * FROM Letter ORDER BY deliverAtEpochSec ASC")
    fun watchAll(): Flow<List<Letter>>

    @Query("SELECT * FROM Letter WHERE id = :id")
    suspend fun get(id: Long): Letter?

    @Query("UPDATE Letter SET delivered = 1 WHERE id = :id")
    suspend fun markDelivered(id: Long)

    @Delete
    suspend fun delete(letter: Letter)

    @Query("DELETE FROM Letter")
    suspend fun deleteAll()
}
