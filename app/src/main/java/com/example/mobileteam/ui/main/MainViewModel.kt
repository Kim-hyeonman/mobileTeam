package com.example.mobileteam.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobileteam.data.model.Activity
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val functions = FirebaseFunctions.getInstance("asia-northeast1")

    private val _recommendations = MutableStateFlow<String>("")
    val recommendations: StateFlow<String> = _recommendations

    private val _weather = MutableStateFlow<String>("")
    val weather: StateFlow<String> = _weather

    private val _address = MutableStateFlow<String>("")
    val address: StateFlow<String> = _address

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    var lat: Double = 0.0
    var lon: Double = 0.0
    fun fetchRecommendations(weather: String, hobbies: List<String>,address: String, considerWeather:Boolean,considerAddress:Boolean) {
        _loading.value = true

        val data = hashMapOf(
            "weather" to weather,
            "hobbies" to hobbies,
            "address" to address,
            "considerWeather" to considerWeather,
            "considerAddress" to considerAddress
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
    fun fetchAddress(lat: Double, lon: Double) {
        _loading.value = true
        this.lat = lat
        this.lon = lon

        val addressData = hashMapOf(
            "lat" to lat,
            "lon" to lon
        )

        Log.d("DEBUG", "fetchAddress called with lat: $lat, lon: $lon")

        functions
            .getHttpsCallable("getAddress")
            .call(addressData)
            .addOnSuccessListener { result ->
                val data = result.data as? Map<*, *>
                val addressStr = data?.get("address") as? String
                _address.value = addressStr ?: "주소를 불러오지 못했습니다."
                _loading.value = false
            }
            .addOnFailureListener { e ->
                _address.value = "주소 요청 실패: ${e.localizedMessage}"
                _loading.value = false
            }
    }
    fun parseAIRecommendation(raw: String): Map<String, List<Activity>> {
        val categoryPattern = Regex("""\d+\. 카테고리: (.+)""")
        val activityTitlePattern = Regex("""\s*\d+\.\s*(.+?)\s*\((.+?)\)""")

        val result = mutableMapOf<String, MutableList<Activity>>()

        var currentCategory: String? = null
        var currentActivity: Activity? = null
        val descriptionBuffer = StringBuilder()

        raw.lines().forEach { line ->
            val trimmed = line.trim()
            // 카테고리 찾기
            val categoryMatch = categoryPattern.find(trimmed)
            if (categoryMatch != null) {
                // 이전 활동이 있으면 저장
                if (currentActivity != null) {
                    currentActivity = currentActivity!!.copy(description = descriptionBuffer.toString().trim())
                    result[currentCategory]?.add(currentActivity!!)
                    descriptionBuffer.clear()
                }
                currentCategory = categoryMatch.groupValues[1]
                result.putIfAbsent(currentCategory!!, mutableListOf())
                currentActivity = null
                descriptionBuffer.clear()
                return@forEach
            }

            // 활동 타이틀 찾기
            val activityMatch = activityTitlePattern.find(line)
            if (activityMatch != null) {
                // 이전 활동 저장
                if (currentActivity != null) {
                    currentActivity = currentActivity!!.copy(description = descriptionBuffer.toString().trim())
                    result[currentCategory]?.add(currentActivity!!)
                    descriptionBuffer.clear()
                }
                val title = activityMatch.groupValues[1]
                val location = activityMatch.groupValues[2]
                currentActivity = Activity(title = title, location = location, description = "")
            } else {
                // 활동 설명 누적
                if (currentActivity != null) {
                    descriptionBuffer.appendLine(trimmed)
                }
            }
        }

        // 마지막 활동 저장
        if (currentActivity != null) {
            currentActivity = currentActivity!!.copy(description = descriptionBuffer.toString().trim())
            result[currentCategory]?.add(currentActivity!!)
        }

        return result
    }

}