package com.igoapp.i_go.feature_note.data.remote.responseDTO

data class PatientDTO(
    val name: String? = null,
    val gender: Boolean? = null,
    val age: Int? = null,
    val blood_type: Int? = null,
    val blood_rh: Boolean? = null,
    val disease: String? = null,
    val extra: String? = null,
    val image: Int? = null,
)