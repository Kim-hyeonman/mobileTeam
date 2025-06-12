package com.example.mobileteam.ui.login


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileteam.data.model.AuthResult
import com.example.mobileteam.data.model.UserData
import com.example.mobileteam.data.repository.AuthRepository
import com.example.mobileteam.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    private val userRepository = UserRepository()
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val listener = FirebaseAuth.AuthStateListener { fbAuth ->
        _isLoggedIn.value = (fbAuth.currentUser != null)
    }
    var currentUser by mutableStateOf<UserData?>(null)


    init { auth.addAuthStateListener(listener)
        if (auth.currentUser != null) {
        // 예: Firebase UID로 DB에서 유저 정보 로드하기
        loadUserData(auth.currentUser!!.uid)
    }}
    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            val userFromDb = userRepository.getUserById(userId)
            currentUser = userFromDb // 불러온 유저 정보를 currentUser에 저장
        }
    }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
    }

    var authResult by mutableStateOf<AuthResult?>(null)
        private set

    suspend fun login(email: String, password: String): AuthResult {
        val result = repository.login(email, password)
        if (result.success) {
            currentUser = result.user// ViewModel에서 상태 업데이트
        }
        authResult = result
        return result
    }



    fun saveUserData(user: UserData) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }
    fun getUserById(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
        }
    }
    fun updateHobbies(hobbies: MutableList<String>) {
        currentUser?.let { user ->
            user.hobbies = hobbies
            saveUserData(user)
        } ?: Log.e("DEBUG", "currentUser is null")
    }
    fun updateUser() {
        currentUser?.let {
            saveUserData(it) // 서버에 저장
        } ?: Log.e("AuthViewModel", "currentUser is null, can't update")
    }


}
