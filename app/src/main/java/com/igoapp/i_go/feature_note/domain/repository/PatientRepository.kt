package com.igoapp.i_go.feature_note.domain.repository

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientMessageDTO
import retrofit2.Response

interface PatientRepository {
    suspend fun getPatients(doctor_id: Int): Response<List<PatientByIdDTO>>

    suspend fun getPatientById(
        doctor_id: Int,
        patient_id: Int
    ): Response<PatientByIdDTO>

    suspend fun insertPatient(
        doctor_id: Int,
        patient: PatientDTO
    ): Response <PatientDTO>

    suspend fun putPatient(
        doctor_id: Int,
        patient_id: Int,
        patient: PatientDTO
    ): Response <PatientDTO>

    suspend fun deletePatient(
        doctor_id: Int,
        patient_id: Int
    ): Response<PatientMessageDTO>

    suspend fun callPatient(
        patient_id: Int,
    ): Response<PatientMessageDTO>
}