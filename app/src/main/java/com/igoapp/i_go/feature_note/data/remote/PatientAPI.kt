package com.igoapp.i_go.feature_note.data.remote

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientMessageDTO
import retrofit2.Response
import retrofit2.http.*

interface PatientAPI {

    // 7. 전체 환자 조회
    @GET("doctor/{doctor_id}/patient/")
    suspend fun getPatients(
        @Path ("doctor_id") doctor_id: Int
    ): Response<List<PatientByIdDTO>>

    // 8. 환자 생성
    @POST("doctor/{doctor_id}/patient/")
    suspend fun insertPatient(
        @Path ("doctor_id") doctor_id: Int,
        @Body patientDTO: PatientDTO
    ): Response<PatientDTO>

    // 9. 환자 한 명 조회
    @GET("doctor/{doctor_id}/patient/{patient_id}/")
    suspend fun getPatientById(
        @Path ("doctor_id") doctor_id: Int,
        @Path ("patient_id") patient_id: Int
    ): Response<PatientByIdDTO>

    // 10. 환자 수정
    @PUT("doctor/{doctor_id}/patient/{patient_id}/")
    suspend fun putPatient(
        @Path ("doctor_id") doctor_id: Int,
        @Path ("patient_id") patient_id: Int,
        @Body patientDTO: PatientDTO
    ): Response<PatientDTO>

    // 11. 환자 삭제
    @DELETE("doctor/{doctor_id}/patient/{patient_id}/")
    suspend fun deletePatient(
        @Path ("doctor_id") doctor_id: Int,
        @Path ("patient_id") patient_id: Int
    ): Response<PatientMessageDTO>

    // 12. 의료진 -> 환자 호출
    @GET("call/doctor/patient/{patient_id}/")
    suspend fun callPatient(
        @Path ("patient_id") patient_id: Int
    ): Response<PatientMessageDTO>
}