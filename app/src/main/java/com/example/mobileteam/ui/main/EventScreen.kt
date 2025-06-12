package com.example.mobileteam.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileteam.R
import com.example.mobileteam.data.model.HobbyViewModel
import com.example.mobileteam.ui.login.AuthViewModel

@Composable
fun EventScreen(
    modifier: Modifier,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    hobbyViewModel: HobbyViewModel,
    onCancel: () -> Unit,
    onMain: () -> Unit
) {
    val userHobbies = authViewModel.currentUser?.hobbies ?: emptyList()
    val weather by mainViewModel.weather.collectAsState()
    val address by mainViewModel.address.collectAsState()
    val allHobbies = remember { hobbyViewModel.hobbies }
    val selectedHobbies = allHobbies.filter { it in userHobbies }
    val filters = remember { mutableStateOf("") }
    val isLoading by mainViewModel.loading.collectAsState()
    val recommendations by mainViewModel.recommendations.collectAsState()
    val parsedMap = mainViewModel.parseAIRecommendation(recommendations)
    val considerWeather by mainViewModel.considerWeather.collectAsState()
    val considerAddress by mainViewModel.considerAddress.collectAsState()
    LaunchedEffect(weather, address, selectedHobbies) {
        val hobbyLabels = selectedHobbies
            .map { it }
            .joinToString(separator = ", ")

        val parts = mutableListOf<String>()
        if (weather.isNotBlank()) parts.add(weather)
        if (address.isNotBlank()) parts.add(address)
        if (hobbyLabels.isNotBlank()) parts.add(hobbyLabels)

        filters.value = parts.joinToString(separator = " • ")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text(
                    text = "취소",
                    color = colorResource(R.color.highlited_blue)
                )
            }
            Text("활동 추천", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = {
                authViewModel.updateUser() // 현재 유저 데이터 저장
                onMain()                   // 메인화면으로 이동
            }) {
                Text("저장", color = colorResource(R.color.highlited_blue))
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedHobbies.forEach { hobby ->
                val selected = hobby in filters.value

                AssistChip(
                    shape = RoundedCornerShape(24.dp),
                    label = { Text(hobby) },
                    onClick = {
                        if (selected) {
                            filters.value = filters.value.replace(hobby, "").trim()
                        } else {
                            filters.value = filters.value + " " + hobby
                        }
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected) colorResource(R.color.highlited_blue)
                        else colorResource(R.color.light_blue),
                        labelColor = if (selected) Color.White
                        else colorResource(R.color.highlited_blue)
                    )
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                shape = RoundedCornerShape(24.dp),
                label = { Text(weather) },
                onClick = {
                    if (weather in filters.value) {
                        filters.value = filters.value.replace(weather, "").trim()
                    } else {
                        filters.value = filters.value + " " + weather
                    }
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (considerWeather) colorResource(R.color.highlited_blue)
                    else colorResource(R.color.light_blue),
                    labelColor = if (considerWeather) Color.White
                    else colorResource(R.color.highlited_blue)
                )
            )
            AssistChip(
                shape = RoundedCornerShape(24.dp),
                label = { Text(address) },
                onClick = {
                    if (address in filters.value) {
                        filters.value = filters.value.replace(address, "").trim()
                    } else {
                        filters.value = filters.value + " " + address
                    }
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (considerAddress) colorResource(R.color.highlited_blue)
                    else colorResource(R.color.light_blue),
                    labelColor = if (considerAddress) Color.White
                    else colorResource(R.color.highlited_blue)
                )
            )
        }

// 활동 보여주기
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                selectedHobbies.forEach { hobby ->
                    val activities = parsedMap[hobby] ?: emptyList()

                    item {
                        Text(
                            text = hobby,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                    }

                    items(activities.size) { index ->
                        val activity = activities[index]
                        val userData = authViewModel.currentUser
                        var liked by remember {
                            mutableStateOf(userData?.favoriteActivities?.contains(activity.title) == true)
                        }

                        // 모든 활동은 활동 리스트에 추가 (중복 방지)
                        LaunchedEffect(Unit) {
                            if (userData != null && !userData.activities.contains(activity.title)) {
                                userData.activities.add(activity.title)
                            }
                        }

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = activity.title,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f) // 남는 공간 차지
                                )
                                IconToggleButton(
                                    checked = liked,
                                    onCheckedChange = { newLiked ->
                                        liked = newLiked
                                        activity.liked = newLiked

                                        userData?.let {
                                            if (newLiked) {
                                                if (!it.favoriteActivities.contains(activity.title)) {
                                                    it.favoriteActivities.add(activity.title)
                                                }
                                            } else {
                                                it.favoriteActivities.remove(activity.title)
                                            }
                                        }
                                    }
                                ) {
                                    val icon = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                                    Icon(imageVector = icon, contentDescription = "Like")
                                }
                            }
                            Text(text = activity.location, style = MaterialTheme.typography.bodySmall)
                            Text(text = activity.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }

    }
}