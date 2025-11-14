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

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    // Flow of all letters (sorted by delivery time)
    val letters: StateFlow<List<Letter>> = repo.lettersFlow()
        .stateIn(
            scope = viewModelScope,
            started =SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList())

    init {
        refreshLetters()
    }

    fun refreshLetters() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repo.syncLetters()
                _uiState.value = UiState.Success
            }
            catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to sync letters.")
            }
        }
    }

    // UI state for single message details
    private val _selectedLetter = MutableStateFlow<Letter?>(null)
    val selectedLetter: StateFlow<Letter?> = _selectedLetter.asStateFlow()

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

    fun clearSelectedLetter() {
        _selectedLetter.value = null

    }
}

sealed interface UiState {
    data object Loading: UiState
    data object Success: UiState
    data class Error(val message: String): UiState
}
