package com.example.mobileteam.navigation

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileteam.ui.AppInfo.AppInfoScreen
import com.example.mobileteam.ui.EditProfile.EditProfileScreen
import com.example.mobileteam.ui.SavedActivities.SavedActivitiesScreen
import com.example.mobileteam.ui.components.MainScaffold
import com.example.mobileteam.ui.login.AuthViewModel
import com.example.mobileteam.ui.login.ChooseInterestScreen
import com.example.mobileteam.ui.login.LoginScreen
import com.example.mobileteam.ui.login.SignupScreen
import com.example.mobileteam.ui.login.WelcomeScreen
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
fun NavGraph(startDestination: String = "login",modifier: Modifier = Modifier,authViewModel: AuthViewModel,mainViewModel: MainViewModel) {

    val navController = rememberNavController()
    NavHost(navController, startDestination) {
        composable("login") {
            val context = LocalContext.current
            LoginScreen(
                authviewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginFail = {
                    Toast.makeText(context,"로그인 실패", Toast.LENGTH_SHORT)
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            SignupScreen(
                authViewModel = authViewModel,
                onSignupClick = { name, email, password ->

                    navController.navigate("interest")
                }
            )
        }
        composable("interest"){
            ChooseInterestScreen(authViewModel = authViewModel) { selectedInterests ->
                navController.navigate("welcome")
            }
        }
        composable("welcome"){
            WelcomeScreen {
                navController.navigate(Screen.Main.route)
            }
        }
        composable(Screen.Main.route) {
            MainScaffold(navController, mainViewModel) {
                MainScreen(mainViewModel, authViewModel)
            }
        }

        composable(Screen.Search.route) {
            MainScaffold(navController, mainViewModel) {
                SearchScreen()
            }
        }

        composable(Screen.UserInfo.route) {
            MainScaffold(navController, mainViewModel) {
                UserInfo(navController, authViewModel)
            }
        }

        composable("user_info") {
            MainScaffold(navController, mainViewModel) {
                UserInfoChangeScreen()
            }
        }

        composable("edit_profile") {
            MainScaffold(navController, mainViewModel) {
                EditProfileScreen()
            }
        }

        composable("saved_activities") {
            MainScaffold(navController, mainViewModel) {
                SavedActivitiesScreen()
            }
        }

        composable("app_info") {
            MainScaffold(navController, mainViewModel) {
                AppInfoScreen()
            }
        }

    }
}