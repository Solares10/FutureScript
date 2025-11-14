package com.example.futurescript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.futurescript.data.LetterRepository

@HiltViewModel
class LetterViewModel @Inject constructor(
    private val repository: LetterRepository
) : ViewModel() {

    fun insert(message: String, deliverAtEpochSec: Long) {
        viewModelScope.launch {
            repository.insertLetter(message, deliverAtEpochSec)
        }
    }

    fun getLetters() = repository.getAllLetters()
}   // 👈 exactly one closing brace here
