package com.example.mobileteam.data.repository

import com.example.mobileteam.data.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult(success = true)
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.localizedMessage)
        }
    }

    suspend fun signup(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult(success = true)
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.localizedMessage)
        }
    }
}