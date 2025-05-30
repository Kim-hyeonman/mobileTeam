package com.example.mobileteam.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobileteam.ui.AppInfo.AppInfoScreen
import com.example.mobileteam.ui.EditProfile.EditProfileScreen
import com.example.mobileteam.ui.SavedActivities.SavedActivitiesScreen
import com.example.mobileteam.ui.login.LoginScreen
import com.example.mobileteam.ui.main.MainScreen
import com.example.mobileteam.ui.main.MainViewModel
import com.example.mobileteam.ui.search.SearchScreen
import com.example.mobileteam.ui.userInfo.UserInfo
import com.example.mobileteam.ui.userInfo.UserInfoChangeScreen

sealed class Screen(val route: String,val icon: ImageVector) {
    object Main : Screen("메인 화면", Icons.Default.Home)
    object Search : Screen("활동 검색", Icons.Default.Search)
    object UserInfo : Screen("내 정보", Icons.Default.Person)
}

@Composable
fun AppNavGraph(navController: NavHostController,mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) { MainScreen(mainViewModel) }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.UserInfo.route) { UserInfo(navController) }
        composable("user_info") { UserInfoChangeScreen() }
        composable("edit_profile") { EditProfileScreen() }
        composable("saved_activities") { SavedActivitiesScreen() }
        composable("app_info") { AppInfoScreen() }
        composable("login") { LoginScreen() }
    }
}

