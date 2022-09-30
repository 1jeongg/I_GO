package com.igoapp.i_go.feature_note.domain.use_case.patient

import com.igoapp.i_go.feature_note.domain.use_case.patient.GetPatients
import javax.inject.Inject

data class PatientUseCases @Inject constructor(
    val getPatients: GetPatients,
    val deletePatient: DeletePatient,
    val callPatient: CallPatient,
    val addPatient: AddPatient,
    val putPatient: PutPatient,
    val getPatientById: GetPatientById
)