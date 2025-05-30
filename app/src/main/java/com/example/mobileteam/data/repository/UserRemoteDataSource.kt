package com.example.mobileteam.data.repository

import android.util.Log
import com.example.mobileteam.data.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun saveUser(user: UserData) {
        try {
            db.collection("users")
                .document(user.userId)
                .set(user)
                .await()
            Log.d("Firestore", "User saved: ${user.userId}, hobbies=${user.hobbies}")
        } catch (e: Exception) {
            Log.e("Firestore", "Save failed: ${e.message}", e)
        }
    }

    suspend fun getUserById(userId: String): UserData? {
        val snapshot = db.collection("users")
            .document(userId)
            .get()
            .await()

        return snapshot.toObject(UserData::class.java)
    }

}
