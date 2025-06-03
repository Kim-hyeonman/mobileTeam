package com.example.mobileteam.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val functions = FirebaseFunctions.getInstance("asia-northeast1")

    private val _recommendations = MutableStateFlow<String>("")
    val recommendations: StateFlow<String> = _recommendations

    private val _weather = MutableStateFlow<String>("")
    val weather: StateFlow<String> = _weather

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    var lat: Double = 0.0
    var lon: Double = 0.0
    fun fetchRecommendations(weather: String, hobbies: List<String>) {
        _loading.value = true

        Log.d("DEBUG", "fetchRecommendations called with weather: $weather, hobbies: $hobbies")
        val data = hashMapOf(
            "weather" to weather,
            "hobbies" to hobbies,
            "lat" to lat,
            "lon" to lon
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
    fun fetchWeather(lat: Double, lon: Double) {
        _loading.value = true
        this.lat = lat  // 클래스 변수에 저장
        this.lon = lon  // 클래스 변수에 저장
        val weatherData = hashMapOf(
            "lat" to lat,
            "lon" to lon
        )
        Log.d("DEBUG", "fetchWeather called with lat: $lat, lon: $lon")
        functions
            .getHttpsCallable("getWeatherByCoordinates")
            .call(weatherData)
            .addOnSuccessListener { result ->
                val weatherInfo = result.data as? Map<*, *>
                val weatherString = weatherInfo?.get("weather")?.toString() ?: "Unknown"
                _weather.value = weatherString
                _loading.value = false
            }
            .addOnFailureListener { e ->
                _weather.value = "Error: ${e.message}"
                _loading.value = false
            }
    }

}