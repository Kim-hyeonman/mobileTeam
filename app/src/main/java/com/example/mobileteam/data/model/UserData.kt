package com.example.mobileteam.data.model

data class UserData(
    val userId: String = "",
    var userName: String = "",
    val userPassword: String = "",
    val favoriteActivities: MutableList<String> = mutableListOf()
)
