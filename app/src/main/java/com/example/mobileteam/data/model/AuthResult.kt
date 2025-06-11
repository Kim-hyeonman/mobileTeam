package com.example.mobileteam.data.model

data class AuthResult(
    val success: Boolean,
    val errorMessage: String? = null,
    val user: UserData? = null
)
