package com.igoapp.i_go.feature_note.presentation.login

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.data.remote.requestDTO.SignInDTO
import com.igoapp.i_go.feature_note.domain.use_case.user.UserUseCases
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
) : ViewModel() {

    private var _signIn = mutableStateOf(SignInDTO())
    val signIn: State<SignInDTO> = _signIn

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val message = ""

    private suspend fun signIn(scaffoldState: ScaffoldState) {
        userUseCases.doSignIn(_signIn.value).collectLatest {
            when (it) {
                is Resource.Success -> {
                    scaffoldState.snackbarHostState.showSnackbar("회원가입 성공")
                    _eventFlow.emit(UiEvent.SignIn)
                }
                is Resource.Error -> {
                    "회원가입 중 에러 발생2".log()
                     scaffoldState.snackbarHostState.showSnackbar("회원가입 정보를 다시 확인해주세요.")
                    _eventFlow.emit(UiEvent.Error(it.message.toString()))
                }
                is Resource.Loading -> {
                    "회원가입 정보 가져오는 중...".log()
                }
            }
        }
    }

    fun onEvent(event: SignInEvent, scaffoldState: ScaffoldState) {
        when (event) {
            is SignInEvent.EnteredUsername -> {
                _signIn.value = signIn.value.copy(
                    username = event.value
                )
            }
            is SignInEvent.EnteredPassword -> {
                _signIn.value = signIn.value.copy(
                    password = event.value
                )
            }
            is SignInEvent.EnteredPassword2 -> {
                _signIn.value = signIn.value.copy(
                    password2 = event.value
                )
            }
            is SignInEvent.EnteredEmail -> {
                _signIn.value = signIn.value.copy(
                    email = event.value
                )
            }
            is SignInEvent.SignIn -> {
                viewModelScope.launch {
                    try {
                        signIn(scaffoldState = scaffoldState)
                    } catch (e: Exception) {
                        "회원가입 중 에러 발생ㅇ".log()
                        _eventFlow.emit(UiEvent.Error(message = "회원가입 중 에러 발생"))
                    }
                }
            }
        }

    }


    sealed class UiEvent {
        data class Error(val message: String) : UiEvent()
        object SignIn : UiEvent()
    }
}