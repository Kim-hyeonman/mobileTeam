package com.example.mobileteam.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(modifier: Modifier,
                 mainViewModel: MainViewModel,
                 authViewModel: AuthViewModel,
                 onCancel: () -> Unit,
                 hobbyViewModel: HobbyViewModel,
                 onApply: () -> Unit
) {
    val weather by mainViewModel.weather.collectAsState()
    val address by mainViewModel.address.collectAsState()
    val allHobbies = remember { hobbyViewModel.hobbies }
    //selectedHobbies null일 경우 오류 날 수 있음
    var selectedHobbies by remember { mutableStateOf(authViewModel.currentUser?.hobbies!!.toMutableSet()) }
    var selectedWeather by remember { mutableStateOf("") }
    var weatherMenuOpen by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf("") }
    var addressMenuOpen by remember { mutableStateOf(false) }
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
            TextButton(onClick = onCancel) { Text("취소", color = colorResource(R.color.highlited_blue)) }
            Text("활동 추천", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(48.dp))
        }

        Divider()


        ExposedDropdownMenuBox(
            expanded = weatherMenuOpen,
            onExpandedChange = { weatherMenuOpen = !weatherMenuOpen },
            modifier = Modifier.background(Color.White)
        ) {
            ListItem(
                headlineContent = { Text("현재 날씨 반영 : ${weather}") },
                supportingContent = {
                    Text(
                        text = if (selectedWeather.isBlank()) "선택 안함" else selectedWeather
                    )
                },
                trailingContent = {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                },

            )

            ExposedDropdownMenu(
                expanded = weatherMenuOpen,
                onDismissRequest = { weatherMenuOpen = false }
            ) {
                listOf("반영", "미반영").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedWeather = option
                            weatherMenuOpen = false
                        }
                    )
                }
            }
        }


        Divider()



        ExposedDropdownMenuBox(
            expanded = addressMenuOpen,
            onExpandedChange = { addressMenuOpen = !addressMenuOpen },
            modifier = Modifier.background(Color.White)
        ) {
            ListItem(
                headlineContent = { Text("현재 위치 반영 : ${address}") },
                supportingContent = {
                    Text(
                        text = if (selectedAddress.isBlank()) "선택 안 함" else selectedAddress
                    )
                },
                trailingContent = {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                },
            )

            ExposedDropdownMenu(
                expanded = addressMenuOpen,
                onDismissRequest = { addressMenuOpen = false }
            ) {
                listOf("반영", "미반영").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedAddress = option
                            addressMenuOpen = false
                        }
                    )
                }
            }
        }
        Divider()

        Text(
            text = "관심사",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
        )

        AssistChip(
            onClick = {},
            label = { Text("${selectedHobbies.size}") },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = colorResource(R.color.highlited_blue),
                labelColor = Color.White
            )
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allHobbies.forEach { hobby ->
                val selected = hobby in selectedHobbies

                AssistChip(
                    shape = RoundedCornerShape(24.dp),
                    label = { Text(hobby) },
                    onClick = {
                        selectedHobbies = selectedHobbies.toMutableSet().apply {
                            if (selected) remove(hobby) else add(hobby)
                        }
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected)
                            colorResource(R.color.highlited_blue)
                        else
                            colorResource(R.color.light_blue),
                        labelColor = if (selected)
                            Color.White
                        else
                            colorResource(R.color.highlited_blue)
                    )
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val hobbies: MutableList<String> = selectedHobbies.toMutableList()
                authViewModel.updateHobbies(hobbies)
                onApply()
                mainViewModel.fetchRecommendations(
                    weather,
                    hobbies,
                    address,
                    selectedWeather == "반영",
                    selectedAddress == "반영"
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.highlited_blue),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { Text("활동 추천받기") }

        Spacer(Modifier.height(16.dp))
    }
}