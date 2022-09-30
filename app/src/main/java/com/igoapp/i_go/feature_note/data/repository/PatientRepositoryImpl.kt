package com.igoapp.i_go.feature_note.data.repository

import com.igoapp.i_go.feature_note.data.remote.PatientAPI
import com.igoapp.i_go.feature_note.data.remote.responseDTO.*
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import dagger.multibindings.IntoMap
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor(
    private val api: PatientAPI
) : PatientRepository {

    override suspend fun getPatients(doctor_id: Int): Response<List<PatientByIdDTO>> {
        return api.getPatients(doctor_id)
    }

    override suspend fun getPatientById(
        doctor_id: Int,
        patient_id: Int
    ): Response<PatientByIdDTO> {
        return api.getPatientById(doctor_id, patient_id)
    }

    override suspend fun insertPatient(
        doctor_id: Int,
        patient: PatientDTO
    ): Response<PatientDTO> {
        return api.insertPatient(doctor_id, patient)
    }

    override suspend fun putPatient(
        doctor_id: Int,
        patient_id: Int,
        patientDTO: PatientDTO
    ): Response<PatientDTO>{
        return api.putPatient(doctor_id, patient_id, patientDTO)
    }

    override suspend fun deletePatient(
        doctor_id: Int,
        patient_id: Int
    ): Response<PatientMessageDTO> {
        return api.deletePatient(doctor_id, patient_id)
    }

    override suspend fun  callPatient(
        patient_id: Int,
    ): Response<PatientMessageDTO> {
        return api.callPatient(patient_id)
    }
}