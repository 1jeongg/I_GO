package com.igoapp.i_go.feature_note.presentation.doctors

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.igoapp.i_go.feature_note.domain.model.ID
import com.igoapp.i_go.feature_note.domain.use_case.user.UserUseCases
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    val userUseCases: UserUseCases
) : ViewModel() {

    private var _user = mutableStateOf(UserDTO())
    var user: State<UserDTO> = _user

    private val _state = mutableStateOf(DoctorState())
    val state: State<DoctorState> = _state

    private var id = mutableStateOf(ID())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            if (!state.value.userResponseDTO.name.isNullOrEmpty()) {
                getUserInfo(state.value.userResponseDTO.user!!)
                _user.value = user.value.copy(
                    name = state.value.userResponseDTO.name!!,
                    subjects = state.value.userResponseDTO.subjects!!,
                    hospital = state.value.userResponseDTO.hospital?.id!!
                )
            }
        }
    }

    private suspend fun putUserInfo(user_id: Int){
        userUseCases.putUserInfo(userDTO = _user.value, doctor_id = user_id).collectLatest {
            when (it) {
                is Resource.Success -> {
                    id.value.userId = user_id
                    id.value.hospitalId = it.data!!.hospital
                    userUseCases.setId(id.value)
                    _eventFlow.emit(UiEvent.SaveDoctor)
                }
                is Resource.Error -> {
                    "환자 정보 저장 중 에러 발생 1".log()
                    _eventFlow.emit(UiEvent.ShowSnackbar("cannot save"))
                }
                is Resource.Loading -> {
                    "환자 정보 값 가져오는 중...".log()
                }
            }
        }
    }
    fun getUserInfo(user_id: Int){
        userUseCases.getUserInfo(user_id).onEach {
            when (it){
                is Resource.Success -> {
                    _state.value = it.data?.let { DoctorState(userResponseDTO = it)}!!
                    _user.value = user.value.copy(
                        name = it.data?.name!!,
                        subjects = it.data.subjects!!,
                        hospital = it.data.hospital?.id!!
                    )
                    "${_user.value}".log()
                }
                is Resource.Error -> {
                    _state.value = DoctorState(error = "doctor get error")
                    "의료진 get 에러".log()
                }
                is Resource.Loading -> {
                    _state.value = DoctorState(isLoading = true)
                    "의료진 로딩중".log()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setFCMToken(token: String){
        _user.value = user.value.copy(
            token = token
        )
    }
    fun onEvent(event: DoctorEvent, doctor_id: Int){
        when (event) {
            is DoctorEvent.EnteredName -> {
                _user.value = user.value.copy(
                    name = event.value
                )
            }
            is DoctorEvent.EnteredMajor -> {
                _user.value = user.value.copy(
                    subjects = event.value
                )
            }
            is DoctorEvent.EnteredHospital -> {
                _user.value = user.value.copy(
                    hospital = event.value
                )
            }
            is DoctorEvent.SaveDoctor -> {
                viewModelScope.launch {
                    try {
                        if ( user.value.name.isBlank()
                            || user.value.subjects.isBlank()
                            || user.value.hospital == 0
                        ) {
                            _eventFlow.emit(UiEvent.ShowSnackbar("모든 칸의 내용을 채워주세요"))
                            return@launch
                        }
                        putUserInfo(doctor_id)
                    } catch (e: Exception) {
                        "의료진 저장 중 에러 발생".log()
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "의료진 정보 저장 중 에러 발생"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveDoctor : UiEvent()
    }


}
