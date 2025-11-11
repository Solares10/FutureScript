package com.example.futurescript.data.auth

import com.example.futurescript.data.auth.model.User

sealed class AuthState {
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Authenticated(val user: User): AuthState()
    data class Error(val message: String): AuthState()
}