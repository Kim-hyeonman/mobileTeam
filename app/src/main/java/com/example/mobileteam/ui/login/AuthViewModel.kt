package com.example.mobileteam.ui.login


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileteam.data.model.AuthResult
import com.example.mobileteam.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val listener = FirebaseAuth.AuthStateListener { fbAuth ->
        _isLoggedIn.value = (fbAuth.currentUser != null)
    }

    init { auth.addAuthStateListener(listener) }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
    }

    var authResult by mutableStateOf<AuthResult?>(null)
        private set

    fun login(email: String, password: String): AuthResult? {
        viewModelScope.launch {
            authResult = repository.login(email, password)
        }
        return authResult
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            authResult = repository.signup(email, password)
        }
    }

}
