package com.igoapp.i_go.feature_note.presentation.patients

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO


sealed class PatientsEvent {
    data class DeletePatient(val patient: PatientDTO): PatientsEvent()
    data class EnteredText(val message: String): PatientsEvent()
    object CallPatient: PatientsEvent()
    object RestorePatients: PatientsEvent()
    object ToggleOrderSection: PatientsEvent()
}