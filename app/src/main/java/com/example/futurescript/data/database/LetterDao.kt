package com.example.futurescript.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.futurescript.data.model.Letter

@Dao
interface LetterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(letter: Letter): Long
    @Query("SELECT * FROM Letter ORDER BY deliverAtEpochSec ASC")
    fun watchAll(): Flow<List<Letter>>
    @Query("UPDATE Letter SET delivered = 1 WHERE id = :id")
    suspend fun markDelivered(id: Long)

    @Query("SELECT * FROM Letter WHERE id = :id")
    suspend fun get(id: Long): Letter?

    @Delete
    suspend fun delete(letter: Letter)

    @Query("DELETE from Letter")
    suspend fun deleteAll()

}