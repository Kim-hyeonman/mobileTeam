package com.example.mobileteam.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mobileteam.navigation.BottomNavigationBar
import com.example.mobileteam.ui.main.MainViewModel

@Composable
fun MainScaffold(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}