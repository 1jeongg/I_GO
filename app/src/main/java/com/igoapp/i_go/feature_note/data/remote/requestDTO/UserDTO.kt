package com.igoapp.i_go.feature_note.data.remote.requestDTO

data class UserDTO(
    var name: String = "",
    var subjects: String = "",
    var hospital: Int = 0,
    var token: String = ""
)