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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
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
    onCancel: () -> Unit
) {
    val userHobbies = authViewModel.currentUser?.hobbies ?: emptyList()

    val weather by mainViewModel.weather.collectAsState()
    val address by mainViewModel.address.collectAsState()
    val allHobbies = remember { hobbyViewModel.hobbies }
    val selectedHobbies = allHobbies.filter { it in userHobbies }
    var filters = remember { mutableStateOf("") }
    val isLoading by mainViewModel.loading.collectAsState()
    val recommendations by mainViewModel.recommendations.collectAsState()

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
            Spacer(Modifier.width(48.dp))
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
                }
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
                }
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

                    item {
                        Text(
                            text = hobby,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                    }
// 활동 목록
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }

    }
}