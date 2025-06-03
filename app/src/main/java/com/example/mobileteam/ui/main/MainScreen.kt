package com.example.mobileteam.ui.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileteam.ui.login.AuthViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel, authViewModel: AuthViewModel) {
    val recommendations by mainViewModel.recommendations.collectAsState()
    val isLoading by mainViewModel.loading.collectAsState()
    val weather by mainViewModel.weather.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                Log.d("DEBUG","weather= ${weather}")
                mainViewModel.fetchRecommendations(
                    weather = weather,
                    hobbies = authViewModel.currentUser?.hobbies ?: emptyList()

                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("추천 활동 불러오기")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(
                text = recommendations,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = weather
            )
        }
    }
}