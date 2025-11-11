package com.example.futurescript.data.repository

import com.example.futurescript.data.database.dao.LetterDao
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.network.model.LetterDto
import com.example.futurescript.data.network.api.FutureScriptApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LetterRepository @Inject constructor(
    private val dao: LetterDao,
    private val api: FutureScriptApiService
) {
    fun lettersFlow(): Flow<List<Letter>> = dao.watchAll()

    suspend fun get(id: Long): Letter? = dao.get(id)

    suspend fun insertLocal(letter: Letter) = dao.insert(letter)

    // Fetches letters from the remote API, converts them to entities, and inserts them into the local database.
    suspend fun syncLetters() {
        try {
            val response = api.getLetters()
            if (response.isSuccessful) {
                // Get list of DTOs from response body
                val remoteLettersDto = response.body() ?: emptyList()
                val localLetters = remoteLettersDto.map {dto -> dto.toEntity()}

                if (localLetters.isNotEmpty()) {
                    dao.insertAll(localLetters)
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(letter: Letter) = dao.delete(letter)

    suspend fun markDelivered(id: Long) = dao.markDelivered(id)

    suspend fun deleteAll() = dao.deleteAll()

}

private fun LetterDto.toEntity(): Letter {
    return Letter(
        id = this.id,
        message = this.message,
        deliverAtEpochSec = this.deliverAtEpochSec,
        createdAtEpochSec = this.createdAtEpochSec,
        delivered = this.isDelivered
    )
}
