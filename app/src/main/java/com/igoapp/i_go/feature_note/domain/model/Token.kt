package com.igoapp.i_go.feature_note.domain.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    var value: String = "",
    var id: Int = 0
)
