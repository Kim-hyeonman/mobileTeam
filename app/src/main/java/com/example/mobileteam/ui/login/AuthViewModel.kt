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
import com.google.firebase.firestore.FirebaseFirestore
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
    fun updateEmail(
        existingEmail: String,
        newEmail: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userDocRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(existingEmail) // 기존 이메일 = 문서 ID

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val emailInDb = document.getString("userId") // 필드에서 확인
                    if (emailInDb == existingEmail) {
                        // Firestore 문서 복사 후 삭제 (문서 ID를 이메일로 쓰기 때문에)
                        val updatedData = document.data?.toMutableMap() ?: mutableMapOf()
                        updatedData["userId"] = newEmail

                        val newDocRef = FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(newEmail)

                        newDocRef.set(updatedData)
                            .addOnSuccessListener {
                                userDocRef.delete() // 기존 문서 삭제
                                currentUser?.userId = newEmail
                                currentUser?.let { saveUserData(it) }
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                onFailure("새 이메일 저장 실패: ${e.message}")
                            }
                    } else {
                        onFailure("기존 이메일이 일치하지 않습니다.")
                    }
                } else {
                    onFailure("기존 이메일이 일치하지 않습니다.")
                }
            }
            .addOnFailureListener { e ->
                onFailure("데이터 조회 실패: ${e.message}")
            }
    }

    fun updatePassword(
        email: String,
        existingPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userDocRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(email)  // 문서 ID가 이메일 주소

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val passwordInDb = document.getString("userPassword")
                    if (passwordInDb == existingPassword) {
                        // 기존 비밀번호 일치 → 비밀번호 업데이트
                        userDocRef.update("userPassword", newPassword)
                            .addOnSuccessListener {
                                currentUser?.userPassword = newPassword
                                currentUser?.let { saveUserData(it) }
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                onFailure("비밀번호 업데이트 실패: ${e.message}")
                            }
                    } else {
                        onFailure("기존 비밀번호가 일치하지 않습니다.")
                    }
                } else {
                    onFailure("기존 비밀번호가 일치하지 않습니다.")
                }
            }
            .addOnFailureListener { e ->
                onFailure("데이터 조회 실패: ${e.message}")
            }
    }
    fun updateName(
        newName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        currentUser?.let { user ->
            user.userName = newName
            saveUserData(user)
            onSuccess() // 무조건 성공했다고 가정
        } ?: onFailure("currentUser is null")
    }

}
