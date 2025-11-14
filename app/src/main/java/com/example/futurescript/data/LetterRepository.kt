package com.example.futurescript.data

import javax.inject.Inject

class LetterRepository @Inject constructor() {

    fun insertLetter(message: String, deliverAtEpochSec: Long) {
        // Placeholder: store the letter somewhere later
    }

    fun getAllLetters(): List<String> {
        return emptyList() // Placeholder: return dummy data
    }
}
