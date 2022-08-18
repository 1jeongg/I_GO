package com.example.i_go.feature_note.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.i_go.feature_note.data.remote.requestDTO.LoginPasswordDTO
import com.example.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.example.i_go.feature_note.domain.model.Token
import com.example.i_go.feature_note.domain.use_case.UserUseCases
import com.example.i_go.feature_note.domain.util.Resource
import com.example.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases

) : ViewModel() {

    private var _user = mutableStateOf(UserDTO())
    val user: State<UserDTO> = _user

    private var _emailPw = mutableStateOf(
        LoginPasswordDTO(
        username = "1jeongg",
        password = "jeongggg"
    )
    )
    val emailPw: State<LoginPasswordDTO> = _emailPw

    private var token = mutableStateOf(Token())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            getUserToken()
            "LoginViewModel init에서 확인한 토큰값 : ${token.value.value}".log()
            "LoginViewModel init에서 확인한 id값 : ${token.value.id}".log()
            if (!token.value.value.isNullOrBlank()) {
                userUseCases.setToken(token.value)
                _eventFlow.emit(UiEvent.Login)
            }
        }
    }

    private suspend fun login() {
        userUseCases.doLogin(_emailPw.value).collectLatest {
            when (it) {
                is Resource.Success -> {
                    token.value = it.data!!
                    userUseCases.setToken(token.value)
                    "LoginViewModel에서 확인한 토큰값 : ${it.data!!.value}".log()
                    "LoginViewModel에서 확인한 id값 : ${token.value.id}".log()
                    _eventFlow.emit(UiEvent.Login)
                }
                is Resource.Error -> {
                    "로그인 중 에러 발생 1".log()
                    _eventFlow.emit(UiEvent.Error("cannot login"))
                }
                is Resource.Loading -> {
                    "토큰 값 가져오는 중...".log()
                }
            }
        }
    }

    suspend fun getUserInfo() {
        userUseCases.getUserInfo().collect() {
            when (it) {
                is Resource.Success -> {
                    _user.value = it.data!!
                }
                is Resource.Error -> {
                    "유저 정보 가져오는 중 에러 발생".log()
                }
                is Resource.Loading -> {
                    "유저 정보 가져오는 중...".log()
                }
            }
        }
    }

    private suspend fun getUserToken() {
        userUseCases.getToken().collect() {
            token.value.value = it.value
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredName -> {
                _emailPw.value = emailPw.value.copy(
                    username = event.value
                )
            }
            is LoginEvent.EnteredPassword -> {
                _emailPw.value = emailPw.value.copy(
                    password = event.value
                )
            }
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    try {
                        if (emailPw.value.username.isNullOrBlank()
                            || emailPw.value.password.isNullOrBlank()
                        ) {
                            "ERROR 입력 되지 않은 칸이 존재".log()
                            _eventFlow.emit(UiEvent.Error(message = "모든 칸의 내용을 채워주세요"))
                            return@launch
                        }
                        login()
                    } catch (e: Exception) {
                        "로그인 중 에러 발생 2".log()
                        _eventFlow.emit(UiEvent.Error(message = "로그인 중 에러 발생"))
                    }
                }
            }
        }

    }

    suspend fun isLoggedIn(): Boolean {
        getUserToken()
        return !token.value.value.isNullOrBlank()
    }

    sealed class UiEvent {
        data class Error(val message: String) : UiEvent()
        object Login : UiEvent()
    }
}