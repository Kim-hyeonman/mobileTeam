package com.example.mobileteam.data.repository

import com.example.mobileteam.data.model.UserData

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) {
    suspend fun saveUser(user: UserData) {
        remoteDataSource.saveUser(user)
    }

    suspend fun getUserById(userId: String): UserData? {
        return remoteDataSource.getUserById(userId)
    }
}
