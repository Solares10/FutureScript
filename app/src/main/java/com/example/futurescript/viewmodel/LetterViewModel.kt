package com.example.futurescript.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.repository.LetterRepository
import com.example.futurescript.workers.scheduleDelivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val repo: LetterRepository,
    private val app: Application
): ViewModel() {

    // Flow of all letters (sorted by delivery time)
    val allLetters: StateFlow<List<Letter>> =
        repo.lettersFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch {
            repo.syncLetters()
        }
    }

    // UI state for single message details
    private val _selectedLetter = MutableStateFlow<Letter?>(null)
    val selectedLetter: StateFlow<Letter?> = _selectedLetter.asStateFlow()


    // Basic CRUD operations
    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(message: String, deliverAt: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val newLetter = Letter(
                message = message.trim(),
                deliverAtEpochSec = deliverAt
            )
            val id = repo.insertLocal(newLetter)
            scheduleDelivery(app, id, deliverAt, message)
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
