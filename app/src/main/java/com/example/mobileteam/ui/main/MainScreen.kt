package com.example.mobileteam.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val recommendations by viewModel.recommendations.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                viewModel.fetchRecommendations(
                    weather = "맑음",
                    favoriteCategories = listOf("등산", "요리")
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
        }
    }
}