package com.example.mobileteam.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileteam.R
import com.example.mobileteam.ui.components.WeatherImage

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    onClick: () -> Unit
) {
    val weather by mainViewModel.weather.collectAsState()
    val address by mainViewModel.address.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // 필요시 여백 조절
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "메인화면",
                fontSize = 18.sp, style = MaterialTheme.typography.titleMedium
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "쉬는날joa",
                fontSize = 24.sp,
                fontWeight = FontWeight.W800,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))

            WeatherImage(weatherMain = weather)

            Spacer(Modifier.height(32.dp))

            Text(
                text = address,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = weather,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

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
                    contentColor = colorResource(id = R.color.white)
                )
            ) {

                Text("추천 활동 불러오기")
            }
        }

    }
}