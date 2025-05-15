package com.example.mobileteam.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val functions = FirebaseFunctions.getInstance("asia-northeast1")

    private val _recommendations = MutableStateFlow<String>("")
    val recommendations: StateFlow<String> = _recommendations

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun fetchRecommendations(weather: String, favoriteCategories: List<String>) {
        _loading.value = true

        val data = hashMapOf(
            "weather" to weather,
            "favoriteCategories" to favoriteCategories
        )

        functions
            .getHttpsCallable("recommendActivitiesWithAI")
            .call(data)
            .addOnSuccessListener { result ->
                val response = result.data as? Map<*, *>
                val text = response?.get("recommendations") as? String
                _recommendations.value = text ?: "추천 결과를 불러오지 못했습니다."
                _loading.value = false
            }
            .addOnFailureListener {
                _recommendations.value = "추천 요청에 실패했습니다: ${it.localizedMessage}"
                _loading.value = false
            }
    }
}