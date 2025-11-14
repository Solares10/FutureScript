package com.example.futurescript.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.repository.LetterRepository
import com.example.futurescript.workers.scheduleDelivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val repo: LetterRepository,
    private val app: Application
) : ViewModel() {

    // --- UI loading/error state management ---
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // --- All letters live flow (auto-updates as DB changes) ---
    val letters: StateFlow<List<Letter>> = repo.watchAllLetters()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Currently selected letter (for detail screen) ---
    private val _selectedLetter = MutableStateFlow<Letter?>(null)
    val selectedLetter: StateFlow<Letter?> = _selectedLetter.asStateFlow()

    init {
        refreshLetters()
    }

    // --- Refresh from repository (used to update UI state) ---
    fun refreshLetters() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repo.syncLetters()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to sync letters: ${e.message}")
            }
        }
    }

    // --- Select and clear letter for viewing ---
    fun selectLetter(id: Long) {
        viewModelScope.launch {
            _selectedLetter.value = repo.getLetter(id)
        }
    }

    fun clearSelectedLetter() {
        _selectedLetter.value = null
    }

    // --- Insert new letter ---
    fun insert(
        message: String,
        deliverAtEpochSec: Long = System.currentTimeMillis() + 86_400_000L // 1 day default
    ) {
        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                val newLetter = Letter(
                    message = message,
                    createdAtEpochSec = now,
                    deliverAtEpochSec = deliverAtEpochSec,
                    delivered = false
                )
                val newId = repo.insert(newLetter)

                // ✅ Only schedule if insertion succeeded
                if (newId > 0) {
                    scheduleDelivery(app, newId, deliverAtEpochSec)
                }

                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to insert letter: ${e.message}")
            }
        }
    }

    // --- Delete single letter ---
    fun delete(letter: Letter) {
        viewModelScope.launch {
            try {
                repo.delete(letter)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to delete letter: ${e.message}")
            }
        }
    }

    // --- Delete all letters ---
    fun deleteAll() {
        viewModelScope.launch {
            try {
                repo.deleteAll()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to delete all letters: ${e.message}")
            }
        }
    }

    // --- Mark a letter as delivered ---
    fun markDelivered(id: Long) {
        viewModelScope.launch {
            try {
                repo.markDelivered(id)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to mark delivered: ${e.message}")
            }
        }
    }
}

// --- Simple UI state representation for observing from UI ---
sealed interface UiState {
    data object Loading : UiState
    data object Success : UiState
    data class Error(val message: String) : UiState
}
