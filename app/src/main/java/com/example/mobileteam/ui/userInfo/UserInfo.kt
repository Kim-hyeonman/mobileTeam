package com.example.mobileteam.ui.userInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileteam.R
import com.example.mobileteam.ui.login.AuthViewModel

@Composable
fun UserInfo(navController: NavController, authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 제목
        Text(
            text = "내 정보",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 프로필 이미지 + 수정 아이콘
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // 이미지 준비 필요
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Blue,
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.White, CircleShape)
                    .padding(3.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // 이름 및 이메일
        Text(text = authViewModel.currentUser!!.userName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = authViewModel.currentUser!!.userId, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        // 버튼 리스트
        Divider()
        MenuItem("내 정보 변경") {
            navController.navigate("edit_profile")
        }
        Divider()
        MenuItem("내가 저장한 활동 list") {
            navController.navigate("saved_activities")
        }
        Divider()
        MenuItem("앱 정보 및 개발자 정보") {
            navController.navigate("app_info")
        }
        Divider()
        MenuItem("로그아웃") {
            navController.navigate("login")
        }
        Divider()
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }
}