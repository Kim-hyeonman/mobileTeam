package com.example.mobileteam.data.model

import androidx.lifecycle.ViewModel

class HobbyViewModel : ViewModel() {
    val hobbies = listOf(
        "게임",
        "음악",
        "영화",
        "애니메이션",
        "산책",
        "요리",
        "패션",
        "전시회",
        "쇼핑",
        "유튜브",
        "독서"
    )
}