package com.example.mobileteam.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobileteam.R

@Composable
fun WeatherImage(weatherMain: String) {
    Log.d("debug",weatherMain)
//    val imageRes = when (weatherMain) {
//
//        "Clear" -> R.drawable.clear
//        "Clouds" -> R.drawable.clouds
//        "Rain" -> R.drawable.rain
//        "Snow" -> R.drawable.snow
//        "Thunderstorm" -> R.drawable.thunderstorm
//        "Drizzle" -> R.drawable.drizzle
//        "Mist", "Haze", "Fog", "Smoke", "Dust", "Sand" -> R.drawable.mist
//        else -> R.drawable.clear// fallback 이미지
//    }
    val imageRes = when {
        weatherMain.contains("Clear", ignoreCase = true) -> R.drawable.clear
        weatherMain.contains("Clouds", ignoreCase = true) -> R.drawable.clouds
        weatherMain.contains("Rain", ignoreCase = true) -> R.drawable.rain
        weatherMain.contains("Snow", ignoreCase = true) -> R.drawable.snow
        weatherMain.contains("Thunderstorm", ignoreCase = true) -> R.drawable.thunderstorm
        weatherMain.contains("Drizzle", ignoreCase = true) -> R.drawable.drizzle
        else -> R.drawable.mist
    }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = weatherMain,
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)

    )
}
