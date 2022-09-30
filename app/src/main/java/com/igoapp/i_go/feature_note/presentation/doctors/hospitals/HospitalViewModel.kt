package com.igoapp.i_go.feature_note.presentation.doctors.hospitals

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDetailDTO
import com.igoapp.i_go.feature_note.domain.use_case.user.GetHospital
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    val getHospitalUseCase: GetHospital,
) : ViewModel() {

    private val _state = mutableStateOf(HospitalState())
    val state: State<HospitalState> = _state

    init {
       getHospital(1)
    }

    fun getHospital(hospital_id: Int) {
        viewModelScope.launch {
            getHospitalUseCase(hospital_id).collect() {
                when (it) {
                    is Resource.Success -> {
                        _state.value =
                            it.data?.let { HospitalState(hospitalDTO = it) }!!
                        "병원 가져오기 성공".log()
                    }
                    is Resource.Error -> "병원 가져오기 실패".log()
                    is Resource.Loading -> "병원 가져오는 중".log()
                }
            }
        }
    }

    fun getHospitalDetail(hospital_id: Int): HospitalDetailDTO {
        getHospital(hospital_id)
        return state.value.hospitalDTO
    }
}