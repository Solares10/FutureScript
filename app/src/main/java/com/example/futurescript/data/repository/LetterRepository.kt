package com.example.futurescript.data.repository

import com.example.futurescript.data.database.LetterDao
import com.example.futurescript.data.model.Letter
import kotlinx.coroutines.flow.Flow

class LetterRepository(private val dao: LetterDao) {
    fun lettersFlow(): Flow<List<Letter>> = dao.watchAll()

    suspend fun get(id: Long): Letter? = dao.get(id)
    suspend fun insert(letter: Letter) = dao.insert(letter)

    suspend fun delete(letter: Letter) = dao.delete(letter)

    suspend fun markDelivered(id: Long) = dao.markDelivered(id)

    suspend fun deleteAll() = dao.deleteAll()

}