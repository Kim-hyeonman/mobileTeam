package com.example.mobileteam.data.repository

import com.example.mobileteam.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun saveUser(user: UserData) {
        db.collection("users")
            .document(user.userId)
            .set(user)
            .await()
    }

    suspend fun getUserById(userId: String): UserData? {
        val snapshot = db.collection("users")
            .document(userId)
            .get()
            .await()

        return snapshot.toObject(UserData::class.java)
    }
}
