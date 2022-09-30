package com.igoapp.i_go.feature_note.data.remote.responseDTO

data class UserResponseDTO(
    val user: Int? = null,
    val hospital: HospitalDTO? = HospitalDTO(),
    val name: String? = "",
    val subjects: String? = ""
)
