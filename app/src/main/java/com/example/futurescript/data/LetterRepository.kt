package com.example.futurescript.data

class LetterRepository(private val dao: LetterDao) {
    fun lettersFlow() = dao.watchAll()
    suspend fun insert(letter: Letter) = dao.insert(letter)
    suspend fun markDelivered(id: Long) = dao.markDelivered(id)
}
