package com.example.futurescript.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.model.Letter
import com.example.futurescript.data.repository.LetterRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LetterViewModel(
    private val repo: LetterRepository
): ViewModel() {

    // Flow of all letters (sorted by delivery time)
    val allLetters: StateFlow<List<Letter>> =
        repo.lettersFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // UI state for single message details
    private val _selectedLetter = MutableStateFlow<Letter?>(null)
    val selectedLetter: StateFlow<Letter?> = _selectedLetter.asStateFlow()


    // Basic CRUD operations
    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(title: String, message: String, deliverAt: Long) {
        viewModelScope.launch {
            val newLetter = Letter(
                title = title.trim(),
                message = message.trim(),
                deliverAtEpochSec = deliverAt
            )
            repo.insert(newLetter)
        }
    }

    fun delete(letter: Letter) {
        viewModelScope.launch {
            repo.delete(letter)
        }
    }

    fun markDelivered(id: Long) {
        viewModelScope.launch {
            repo.markDelivered(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }

    fun selectLetter(id: Long) {
        viewModelScope.launch {
            val letter = repo.get(id)
            _selectedLetter.value = letter
        }
    }
}