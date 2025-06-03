package com.example.mobileteam.data.model

import androidx.compose.runtime.mutableStateListOf

data class UserData(
    val userId: String = "",
    var userName: String = "",
    val userPassword: String = "",
    var hobbies : MutableList<String> = mutableStateListOf(),
    val favoriteActivities: MutableList<String> = mutableStateListOf()
)
