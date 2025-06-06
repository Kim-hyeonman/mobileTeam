package com.example.mobileteam.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.mobileteam.R
import com.example.mobileteam.ui.login.AuthViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel,
               onClick: ()->Unit) {
//    val recommendations by mainViewModel.recommendations.collectAsState()
//    val isLoading by mainViewModel.loading.collectAsState()
    val weather by mainViewModel.weather.collectAsState()
    val address by mainViewModel.address.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "쉬는날joa",
            fontSize = 24.sp,
            fontWeight = FontWeight.W800,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(32.dp))

        AsyncImage(
            model = R.drawable.cloud,
            contentDescription = "구름 많음",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(32.dp))

        Text(text = address,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(text = weather,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(40.dp))

        Button(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .widthIn(min = 220.dp)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                Log.d("DEBUG", "weather= ${weather}")
//                mainViewModel.fetchRecommendations(
//                    weather = weather,
//                    hobbies = authViewModel.currentUser?.hobbies ?: emptyList()
//
//                )
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.highlited_blue),
                contentColor   = colorResource(id = R.color.white)
            )
        ) {

            Text("추천 활동 불러오기")
        }

//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//        } else {
//            Text(
//                text = recommendations,
//                style = MaterialTheme.typography.bodyLarge
//            )
//
//        }
    }
}