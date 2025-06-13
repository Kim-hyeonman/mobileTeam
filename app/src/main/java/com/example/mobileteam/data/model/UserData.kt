package com.example.mobileteam.data.model

import androidx.compose.runtime.mutableStateListOf

data class UserData(
    var userId: String = "",
    var userName: String = "",
    var userPassword: String = "",
    var hobbies : MutableList<String> = mutableStateListOf(),
    var activities : MutableList<String> = mutableStateListOf(),
    val favoriteActivities: MutableList<String> = mutableStateListOf()
)
