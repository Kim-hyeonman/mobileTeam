package com.example.mobileteam.data.model

data class Activity(
    val title: String,
    val location: String,
    val description: String,
    var liked: Boolean = false  // 좋아요 상태를 저장할 필드
)