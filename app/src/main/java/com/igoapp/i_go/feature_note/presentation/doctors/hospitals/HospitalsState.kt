package com.igoapp.i_go.feature_note.presentation.doctors.hospitals

import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDTO

data class HospitalsState(
    val isLoading: Boolean = false,
    val hospitalDTOs: List<HospitalDTO> = emptyList(),
    val error: String = ""
)
