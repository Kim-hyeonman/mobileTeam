package com.example.mobileteam.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Main,
        Screen.Search,
        Screen.UserInfo
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Gray)
    {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val selectedColor = Color(0xFF006FFD)

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        tint = LocalContentColor.current
                    )
                },
                label = { Text(screen.route) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = selectedColor,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = true
            )
        }
    }
}

