package com.example.mobileteam.ui.savedActivities

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mobileteam.ui.login.AuthViewModel

@Composable
fun SavedActivitiesScreen(authViewModel: AuthViewModel) {
    LazyColumn {
        val favoriteActivities = authViewModel.currentUser?.favoriteActivities ?: emptyList()

        items(favoriteActivities.size) { index ->
            Text(text = favoriteActivities[index])
        }
    }
}