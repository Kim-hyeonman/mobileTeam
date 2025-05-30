package com.example.mobileteam.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileteam.ui.login.AuthViewModel
import com.example.mobileteam.ui.login.ChooseInterestScreen
import com.example.mobileteam.ui.login.LoginScreen
import com.example.mobileteam.ui.login.SignupScreen
import com.example.mobileteam.ui.login.WelcomeScreen
import com.example.mobileteam.ui.main.MainScreen
import com.example.mobileteam.ui.main.MainViewModel

@Composable
fun NavGraph(startDestination: String = "login",modifier: Modifier = Modifier) {
    val authviewModel: AuthViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController, startDestination) {
        composable("login") {
            val context = LocalContext.current
            LoginScreen(
                viewModel = authviewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onLoginFail = {
                    Toast.makeText(context,"로그인 실패",Toast.LENGTH_SHORT)
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }
        composable("signup") {
            SignupScreen(
                onSignupClick = { name, email, password ->
                    authviewModel.signup(email, password)
                    navController.navigate("interest")
                }
            )
        }
        composable("interest"){
            ChooseInterestScreen(){
                navController.navigate("welcome")
            }
        }
        composable("welcome"){
            WelcomeScreen {
                navController.navigate("home")
            }
        }
        composable("home") {
            MainScreen(mainViewModel)
        }
    }
}