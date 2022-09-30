package com.igoapp.i_go.feature_note.presentation.doctors.hospitals

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.domain.use_case.user.GetHospitals
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalsViewModel @Inject constructor(
    val getHospitalsUseCase: GetHospitals,
) : ViewModel() {

    private val _state = mutableStateOf(HospitalsState())
    val state: State<HospitalsState> = _state

    init {
        getHospitals()
    }

    fun getHospitals() {
        viewModelScope.launch {
            getHospitalsUseCase().collect() {
                when (it) {
                    is Resource.Success -> {
                        _state.value =
                            it.data?.let { HospitalsState(hospitalDTOs = it) }!!
                        "병원 목록 가져오기 성공".log()
                    }
                    is Resource.Error -> "병원 목록 가져오기 실패".log()
                    is Resource.Loading -> "병원 목록 가져오는 중".log()
                }
            }
        }
    }
}