package com.example.futurescript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurescript.data.repository.UserRepository
import com.example.futurescript.data.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                }
                else {
                    _authState.value = AuthState.Error("Login failed: Invalid credentials.")
                }
            }
            catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed.")
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.signUp(email, password)
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                }
                else {
                    _authState.value = AuthState.Error("Signup failed: User may already exist.")
                }
            }
            catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Signup failed.")
            }
        }
    }

    fun logOut() {
        userRepository.logOut()
        _authState.value = AuthState.Unauthenticated
    }
}