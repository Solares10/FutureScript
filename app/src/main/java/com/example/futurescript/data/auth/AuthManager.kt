package com.example.futurescript.data.auth

import com.example.futurescript.data.auth.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(): AuthManager {
        return AuthManager()
    }
}

class AuthManager(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    fun getAuthState(): Flow<AuthState> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                trySend(
                    AuthState.Authenticated(
                        User(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: ""
                        ),

                    )
                )
            }
            else {
                trySend(AuthState.Unauthenticated)
            }
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
        }

    suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    fun logOut() {
        auth.signOut()
    }
}

