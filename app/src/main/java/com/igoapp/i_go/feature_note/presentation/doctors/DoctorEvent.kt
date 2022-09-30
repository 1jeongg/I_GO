package com.igoapp.i_go.feature_note.presentation.doctors


sealed class DoctorEvent {
    data class EnteredName(val value: String): DoctorEvent()
    data class EnteredMajor(val value: String): DoctorEvent()
    data class EnteredHospital(val value: Int): DoctorEvent()

    object SaveDoctor: DoctorEvent()
}
