package com.example.i_go.feature_note.presentation.patients

import com.example.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO

data class PatientsState (
    val isLoading: Boolean = false,
    val patientDTOS: List<PatientByIdDTO> = emptyList(),
    val error: String = "",
)