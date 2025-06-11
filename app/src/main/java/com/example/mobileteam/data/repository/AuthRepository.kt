package com.example.mobileteam.data.repository

import android.util.Log
import com.example.mobileteam.data.model.AuthResult
import com.example.mobileteam.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val db = FirebaseFirestore.getInstance()
    private suspend fun getUserByEmail(email: String): UserData? {
        val querySnapshot = db.collection("users")
            .whereEqualTo("userId", email)
            .limit(1)
            .get()
            .await()

        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].toObject(UserData::class.java)
        } else {
            null
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val user = getUserByEmail(email)
            Log.d("DEBUG", "user: $user")
            if (user == null) {
                AuthResult(success = false, errorMessage = "사용자를 찾을 수 없습니다.")
            } else if (user.userPassword != password) {
                AuthResult(success = false, errorMessage = "비밀번호가 올바르지 않습니다.")
            } else {
                AuthResult(success = true, user = user)
            }
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.localizedMessage)
        }
    }
}