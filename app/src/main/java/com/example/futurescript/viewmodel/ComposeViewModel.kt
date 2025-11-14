package com.example.futurescript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.repository.LetterRepository
import com.example.futurescript.data.database.entities.Letter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeViewModel @Inject constructor(
    private val repo: LetterRepository
) : ViewModel() {

    fun insert(message: String, dateDelivered: Long) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val letter = Letter(
                message = message,
                createdAtEpochSec = now,
                deliverAtEpochSec = now + 86400000L, // e.g. 1 day later, adjust as needed
                delivered = false
            )

        }
    }
}