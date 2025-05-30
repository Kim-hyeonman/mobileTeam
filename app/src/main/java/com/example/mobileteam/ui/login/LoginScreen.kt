package com.example.mobileteam.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mobileteam.R

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSignupClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginFail: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        Image(
            painter = painterResource(R.drawable.login),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .border(1.dp, Color.Blue, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "쉬는날joa 로그인",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일 주소") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.AddCircle else Icons.Default.ArrowDropDown ,
                            contentDescription = null
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = {
                    val authResult = viewModel.login(email, password)
                    authResult?.let {
                        if(authResult.success) onLoginSuccess else onLoginFail
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("로그인", color = Color.White)
            }
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("쉬는날joa가 처음이세요? ")
                Text(
                    text = "회원가입",
                    color = Color(0xFF007BFF),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignupClick() }
                )
            }
        }
    }
}

