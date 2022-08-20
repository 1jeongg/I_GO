package com.example.i_go.feature_note.domain.repository

import com.example.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.example.i_go.feature_note.data.remote.responseDTO.PatientDTO
import retrofit2.Response

interface PatientRepository {
    suspend fun getPatients(doctor_id: Int): Response<List<PatientByIdDTO>>

    suspend fun getPatientById(id: Int): PatientDTO?

    suspend fun insertPatient(patient: PatientDTO)

    suspend fun deletePatient(patient: PatientDTO)
}