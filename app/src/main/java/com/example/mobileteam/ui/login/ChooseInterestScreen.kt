package com.example.mobileteam.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChooseInterestScreen(onNextClick: (List<String>) -> Unit) {
    val interests = listOf("게임", "독서", "여행", "영화", "음악", "야구", "유튜브", "산책")
    val selectedInterests = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp)
        .verticalScroll(rememberScrollState())) {

        Spacer(modifier = Modifier.height(40.dp))

        StepProgressBar(currentStep = 2, totalSteps = 3)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "당신에게 개인화된 컨텐츠를 제공합니다.",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            "관심사를 골라보세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        interests.forEach { interest ->
            val isSelected = interest in selectedInterests

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (isSelected) selectedInterests.remove(interest)
                        else selectedInterests.add(interest)
                    },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color(0xFFE8F1FF) else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFF007BFF) else Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = interest,
                        modifier = Modifier.weight(1f),
                        color = Color.Black
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF007BFF)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onNextClick(selectedInterests) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Text("다음", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

}