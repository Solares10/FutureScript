package com.example.futurescript.data.repository

import com.example.futurescript.data.auth.AuthManager
import com.google.firebase.auth.FirebaseUser
import com.example.futurescript.data.auth.model.User


fun FirebaseUser?.toUser(): User {
    return User(
        uid = this?.uid.orEmpty(),
        email = this?.email.orEmpty(),
        displayName = this?.displayName.orEmpty(),
    )
}

class UserRepository(private val authManager: AuthManager = AuthManager()) {
    val currentUser: User? get() = authManager.currentUser?.toUser()

    suspend fun login(email: String, password: String): User? {
        return authManager.login(email, password)?.toUser()
    }

    suspend fun signUp(email: String, password: String): User? {
        return authManager.signUp(email, password)?.toUser()
    }

    fun logOut() {
        authManager.logOut()
    }

}