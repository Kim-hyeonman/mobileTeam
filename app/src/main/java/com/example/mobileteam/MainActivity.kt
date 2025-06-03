package com.example.mobileteam

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.mobileteam.navigation.NavGraph
import com.example.mobileteam.ui.login.AuthViewModel
import com.example.mobileteam.ui.main.MainViewModel
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation { lat, lon ->
                Log.d("MainActivity", "현재 위치: lat=$lat, lon=$lon")
                viewModel.fetchWeather(lat, lon)
            }
        } else {
            Log.e("MainActivity", "위치 권한 거부됨")
        }
    }
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isLoggedIn = authViewModel.isLoggedIn.value
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // 이미 권한 있음 → 바로 위치 요청
            getCurrentLocation { lat, lon ->
                Log.d("MainActivity", "현재 위치: lat=$lat, lon=$lon")
                viewModel.fetchWeather(lat, lon)
            }
        } else {
            // 권한 요청 (이후 콜백에서 처리됨)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setContent {
            Surface(modifier = Modifier) {

                NavGraph(startDestination = "login",authViewModel = authViewModel,mainViewModel = viewModel)

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    onLocationReceived(it.latitude, it.longitude)
                } ?: run {
                    Log.e("MainActivity", "위치 정보를 가져올 수 없음")
                }
            }
            .addOnFailureListener {
                Log.e("MainActivity", "위치 요청 실패: ${it.localizedMessage}")
            }
    }
}

