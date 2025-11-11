package com.example.futurescript.data.repository

import com.example.futurescript.data.database.dao.LetterDao
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.network.api.FutureScriptApiService
import kotlinx.coroutines.flow.Flow

class LetterRepository(
    private val dao: LetterDao,
    private val api: FutureScriptApiService
) {
    fun lettersFlow(): Flow<List<Letter>> = dao.watchAll()

    suspend fun get(id: Long): Letter? = dao.get(id)

    suspend fun insertLocal(letter: Letter) = dao.insert(letter)

    suspend fun syncLetters() {
        val letters = api.getLetters()
        if (letters.isSuccessful) {
            val remoteLetters = letters.body() ?: emptyList()
            // TODO: Convert Dto to Entity and insert into Room
        }
    }

    suspend fun delete(letter: Letter) = dao.delete(letter)

    suspend fun markDelivered(id: Long) = dao.markDelivered(id)

    suspend fun deleteAll() = dao.deleteAll()

}
