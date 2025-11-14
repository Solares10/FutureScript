package com.example.futurescript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.repository.LetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val repo: LetterRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)


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
                _uiState.value = UiState.Error(e.message ?: "Failed to sync letters.")
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
