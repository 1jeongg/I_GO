package com.example.i_go.feature_note.domain.use_case.patient

import com.example.i_go.feature_note.domain.use_case.patient.GetPatients
import javax.inject.Inject

data class PatientUseCases @Inject constructor(
    val getPatients: GetPatients,
)