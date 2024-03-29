package com.igoapp.i_go.feature_note.presentation.add_edit_patient

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO

data class PatientState (
    val isLoading: Boolean = false,
    var patientByIdDTO: PatientByIdDTO = PatientByIdDTO(),
    val error: String = "",
)