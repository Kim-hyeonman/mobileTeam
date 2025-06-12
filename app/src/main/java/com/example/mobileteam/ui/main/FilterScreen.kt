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
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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

@Composable
fun FilterScreen(
    modifier: Modifier,
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
    val considerWeather by mainViewModel.considerWeather.collectAsState()
    val considerAddress by mainViewModel.considerAddress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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
                    "취소",
                    color = colorResource(R.color.highlited_blue)
                )
            }
            Text("활동 추천", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(48.dp))
        }

        Divider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "날씨 고려",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
            )
            Switch(
                checked = considerWeather,
                onCheckedChange = {newValue ->
                    mainViewModel.onCheckWeatherChanged(newValue)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = colorResource(R.color.highlited_blue),
                    uncheckedTrackColor = colorResource(R.color.light_blue),
                    checkedTrackColor = colorResource(R.color.highlited_blue),
                )
            )
        }

        Divider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "위치 고려",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
            )

            Switch(
                checked = considerAddress,
                onCheckedChange = {newValue ->
                    mainViewModel.onCheckAddressChanged(newValue)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = colorResource(R.color.highlited_blue),
                    uncheckedTrackColor = colorResource(R.color.light_blue),
                    checkedTrackColor = colorResource(R.color.highlited_blue),
                )
            )
        }

        Divider()

        Text(
            text = "관심사",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
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
                    weather = weather,
                    hobbies = hobbies,
                    address = address,
                    considerWeather = considerWeather,
                    considerAddress = considerAddress
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