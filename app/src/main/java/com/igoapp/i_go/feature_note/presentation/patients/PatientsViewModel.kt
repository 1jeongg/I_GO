package com.igoapp.i_go.feature_note.presentation.patients

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientMessageDTO
import com.igoapp.i_go.feature_note.domain.use_case.patient.PatientUseCases
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import com.igoapp.i_go.feature_note.presentation.add_edit_patient.AddEditPatientViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor (
    private val patientUseCases: PatientUseCases
) : ViewModel() {

    private val _state = mutableStateOf(PatientsState())
    val state: State<PatientsState> = _state

    private var recentlyDeletedPatient: PatientDTO? = null

    private var _message = mutableStateOf(PatientMessageDTO(message = ""))
    val message: State<PatientMessageDTO> = _message

    private val _eventFlow = MutableSharedFlow<AddEditPatientViewModel.UiEvent>()

    init {
        getPatients(0)
    }

    fun onEvent(event: PatientsEvent, doctor_id: Int, patient_id: Int) {
        when (event) {
            is PatientsEvent.EnteredText -> {
                _message.value = message.value.copy(
                    message = event.message
                )
            }
            is PatientsEvent.DeletePatient -> {
                "여기 환자 삭제된 거 ${event.patient}".log()
                recentlyDeletedPatient = event.patient
                patientUseCases.deletePatient(doctor_id, patient_id).launchIn(viewModelScope)
            }
            is PatientsEvent.CallPatient -> {
                patientUseCases.callPatient(patient_id).launchIn(viewModelScope)
            }

            is PatientsEvent.RestorePatients -> {
                viewModelScope.launch {
                    try {
                        addPatient(doctor_id = doctor_id, patientDTO = recentlyDeletedPatient!!)
                    } catch(e: Exception) {
                        _eventFlow.emit(
                            AddEditPatientViewModel.UiEvent.ShowSnackbar(
                                message = e.message ?: "환자 정보를 저장할 수 없습니다."
                            )
                        )
                    }
                }
                recentlyDeletedPatient = null
            }
        }
    }

    private suspend fun addPatient(doctor_id: Int, patientDTO: PatientDTO){
        patientUseCases.addPatient(
            doctor_id = doctor_id,
            patientDTO = recentlyDeletedPatient!!
        ).collectLatest {
            when (it){
                is Resource.Success -> {
                    _eventFlow.emit(AddEditPatientViewModel.UiEvent.SavePatient)
                }
                is Resource.Error -> {
                    "환자 추가 중 에러 발생 1".log()
                    _eventFlow.emit(AddEditPatientViewModel.UiEvent.ShowSnackbar("환자 추가 실패"))
                }
                is Resource.Loading -> {
                    "환자 추가하는 중...".log()
                }
            }
        }
        recentlyDeletedPatient = null
    }
    fun getPatients(user_id: Int) {
        patientUseCases.getPatients(user_id).onEach { result ->
            when (result){
                is Resource.Success -> {
                    _state.value = result.data?.let { PatientsState(patientDTOS = it) }!!
                }
                is Resource.Error -> {
                    _state.value = PatientsState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = PatientsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}