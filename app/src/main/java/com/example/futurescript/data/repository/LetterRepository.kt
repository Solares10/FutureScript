package com.example.futurescript.data.repository

import com.example.futurescript.data.database.dao.LetterDao
import com.example.futurescript.data.database.entities.Letter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LetterRepository @Inject constructor(
    private val dao: LetterDao
) {
    // --- Watch all letters (sorted by delivery date) ---
    fun watchAllLetters(): Flow<List<Letter>> = dao.watchAll()

    // --- Insert new letter and return its new ID ---
    suspend fun insert(letter: Letter): Long {
        return dao.insert(letter)
    }

    // --- Delete a single letter ---
    suspend fun delete(letter: Letter) {
        dao.delete(letter)
    }

    // --- Delete all letters ---
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    // --- Get a letter by ID ---
    suspend fun getLetter(id: Long): Letter? {
        return dao.get(id)
    }

    // --- Mark a letter as delivered ---
    suspend fun markDelivered(id: Long) {
        dao.markDelivered(id)
    }

    // --- Sync placeholder (if you add remote sync later) ---
    suspend fun syncLetters() {
        // Currently local only
    }
}
