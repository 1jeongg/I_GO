package com.igoapp.i_go.feature_note.presentation.add_edit_patient

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.domain.use_case.patient.PatientUseCases
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditPatientViewModel @Inject constructor (
    private val patientUseCases: PatientUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _patientName = mutableStateOf(PatientTextFieldState(
        hint = "홍길동"
    ))
    val patientName: State<PatientTextFieldState> = _patientName

    private val _patientSex = mutableStateOf(PatientRadioButtonState())
    val patientSex: State<PatientRadioButtonState> = _patientSex

    private val _patientAge = mutableStateOf(PatientTextFieldState(
        hint = "70세"
    ))
    val patientAge: State<PatientTextFieldState> = _patientAge

    private val _patientBloodType = mutableStateOf(PatientRadioButtonState())
    val patientBloodType: State<PatientRadioButtonState> = _patientBloodType

    private val _patientBloodRh = mutableStateOf(PatientRadioButtonState())
    val patientBloodRh: State<PatientRadioButtonState> = _patientBloodRh

    private val _patientDiseases = mutableStateOf(PatientTextFieldState(
        hint = "치매"
    ))
    val patientDiseases: State<PatientTextFieldState> = _patientDiseases

    private val _patientExtra = mutableStateOf(PatientTextFieldState(
        hint = "어르신 거동이 불편하심"
    ))
    val patientExtra: State<PatientTextFieldState> = _patientExtra

    private val _patientImage = mutableStateOf(PatientByIdDTO.patientImages.random())
    val patientImage: State<Int> = _patientImage

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(PatientState())
    val state: State<PatientState> = _state

    var patientId = mutableStateOf(0)
    var doctorId = mutableStateOf(0)

    init {
        savedStateHandle.get<Int>("patientId")?.let { patient_id ->
            if(patient_id > 0) {
                viewModelScope.launch {
                    patientId.value = patient_id
                }
            }
        }
    }
    fun setDoctorId(doctor_id: Int){
        doctorId.value = doctor_id
    }
    fun getPatient(doctor_id: Int){
        patientUseCases.getPatientById(
            doctor_id,
            patientId.value
        ).onEach { patient ->
            when (patient) {
                is Resource.Success -> {
                    _patientName.value = patientName.value.copy(
                        text = patient.data?.name!!,
                        isHintVisible = false
                    )
                    _patientSex.value = _patientSex.value.copy(
                        bool_text = patient.data.gender!!
                    )
                    _patientAge.value = _patientAge.value.copy(
                        text = patient.data.age.toString(),
                        isHintVisible = false
                    )
                    _patientBloodType.value = _patientBloodType.value.copy(
                        int_text = patient.data.blood_type!!
                    )
                    _patientBloodRh.value = _patientBloodType.value.copy(
                        int_text = patient.data.blood_type
                    )
                    _patientDiseases.value = _patientDiseases.value.copy(
                        text = patient.data.disease!!,
                        isHintVisible = false
                    )
                    _patientExtra.value = _patientExtra.value.copy(
                        text = patient.data.extra!!,
                        isHintVisible = false
                    )
                    _patientImage.value = patient.data.image!!
                }
                is Resource.Error -> {
                    _state.value =
                        PatientState(error = patient.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _state.value = PatientState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun addPatient(){
        patientUseCases.addPatient(
            doctorId.value,
            PatientDTO(
                name = patientName.value.text,
                gender = patientSex.value.bool_text,
                age = patientAge.value.text.toInt(),
                blood_type = patientBloodType.value.int_text,
                blood_rh = patientBloodRh.value.bool_text,
                disease = patientDiseases.value.text,
                extra = patientExtra.value.text,
                image = patientImage.value + 1,
            )
        ).collectLatest {
            when (it){
                is Resource.Success -> {
                   "환자 정보 add 성공".log()
                    _eventFlow.emit(UiEvent.SavePatient)
                }
                is Resource.Error -> {
                    "환자 정보 add 중 에러 발생".log()
                    _eventFlow.emit(UiEvent.ShowSnackbar("cannot save"))
                }
                is Resource.Loading -> {
                    "환자 정보 add 중...".log()
                }
            }
        }
    }
    private suspend fun putPatient(){
        patientUseCases.putPatient(
            doctorId.value,
            patientId.value,
            PatientDTO(
                name = patientName.value.text,
                gender = patientSex.value.bool_text,
                age = patientAge.value.text.toInt(),
                blood_type = patientBloodType.value.int_text,
                blood_rh = patientBloodRh.value.bool_text,
                disease = patientDiseases.value.text,
                extra = patientExtra.value.text,
                image = patientImage.value + 1,
            )
        ).collectLatest {
            when (it){
                is Resource.Success -> {
                     "환자 정보 put 성공".log()
                    _eventFlow.emit(UiEvent.SavePatient)
                }
                is Resource.Error -> {
                    "환자 정보 put 중 에러 발생".log()
                    _eventFlow.emit(UiEvent.ShowSnackbar("cannot save"))
                }
                is Resource.Loading -> {
                    "환자 정보 put 중...".log()
                }
            }
        }
    }
    fun onEvent(event: AddEditPatientEvent) {
        when(event) {
            is AddEditPatientEvent.EnteredName -> {
                _patientName.value = patientName.value.copy(
                    text = event.value
                )
            }
            is AddEditPatientEvent.ChangeNameFocus -> {
                _patientName.value = patientName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            patientName.value.text.isBlank()
                )
            }
            is AddEditPatientEvent.EnteredSex -> {
                _patientSex.value = _patientSex.value.copy(
                    bool_text = event.value == "남자"
                )
            }
            is AddEditPatientEvent.EnteredAge -> {
                _patientAge.value = _patientAge.value.copy(
                    text = event.value
                )
            }
            is AddEditPatientEvent.ChangeAgeFocus -> {
                _patientAge.value = _patientAge.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _patientAge.value.text.isBlank()
                )
            }
            is AddEditPatientEvent.EnteredBloodType -> {
                _patientBloodType.value = _patientBloodType.value.copy(
                    int_text = if(event.value == "A형") 0 else if (event.value == "B형") 1 else if (event.value == "O형") 2 else 3
                )
            }
            is AddEditPatientEvent.EnteredBloodRh -> {
                _patientBloodRh.value = _patientBloodRh.value.copy(
                    bool_text = event.value == "Rh +"
                )
            }
            is AddEditPatientEvent.EnteredDiseases -> {
                _patientDiseases.value = _patientDiseases.value.copy(
                    text = event.value
                )
            }
            is AddEditPatientEvent.ChangeDiseasesFocus -> {
                _patientDiseases.value = _patientDiseases.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _patientDiseases.value.text.isBlank()
                )
            }
            is AddEditPatientEvent.EnteredExtra -> {
                _patientExtra.value = _patientExtra.value.copy(
                    text = event.value
                )
            }
            is AddEditPatientEvent.ChangeExtraFocus -> {
                _patientExtra.value = _patientExtra.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _patientExtra.value.text.isBlank()
                )
            }

            is AddEditPatientEvent.ChangeImage -> {
                _patientImage.value = event.image
            }
            is AddEditPatientEvent.SavePatient -> {
                viewModelScope.launch {
                    try {
                        if (patientId.value <= 0) addPatient()
                        else putPatient()

                    } catch(e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "환자 정보를 저장할 수 없습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SavePatient: UiEvent()
    }
}