package com.example.i_go.feature_note.presentation.doctors.hospitals

import com.example.i_go.feature_note.data.remote.responseDTO.HospitalDetailDTO

data class HospitalState(
    val isLoading: Boolean = false,
    val hospitalDTO: HospitalDetailDTO = HospitalDetailDTO(),
    val error: String = ""
)
