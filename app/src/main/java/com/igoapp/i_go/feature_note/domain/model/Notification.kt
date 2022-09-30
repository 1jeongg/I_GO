package com.igoapp.i_go.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.igoapp.i_go.R

@Entity
data class Notification(
    val name: String,
    val patient_id: Int,
    val patient_x: Double,
    val patient_y: Double,
    val image: Int,
    @PrimaryKey val id: Int? = null
)
