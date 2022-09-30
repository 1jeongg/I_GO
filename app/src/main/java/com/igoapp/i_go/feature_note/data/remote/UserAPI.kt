package com.igoapp.i_go.feature_note.data.remote

import com.igoapp.i_go.feature_note.data.remote.requestDTO.LoginPasswordDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.SignInDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDetailDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.SignInResponseDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.UserResponseDTO
import com.igoapp.i_go.feature_note.domain.model.Token
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    // 1. 병원 전체 조회
    @GET("hospital/")
    suspend fun getHospitals(): Response<List<HospitalDTO>>

    // 2. 병원 1개 조회
    @GET("hospital/{hospital_id}/")
    suspend fun getHospital(
        @Path("hospital_id") hospital_id: Int,
    ): Response<HospitalDetailDTO>

    // 3. 회원가입
    @POST("register/")
    suspend fun signIn(
        @Body signInDTO : SignInDTO
    ): Response<SignInResponseDTO>

    // 4. 로그인
    @POST("login/")
    suspend fun login(
        @Body loginpasswordDTO : LoginPasswordDTO
    ): Response<Token>

    // 5. 의료진 프로필 수정
    @PUT("doctor/{doctor_id}/")
    suspend fun putUserInfo(
        @Path("doctor_id") doctor_id: Int,
        @Body userDTO: UserDTO
    ): Response<UserDTO>

    // 6. 의료진 프로필 조회
    @GET("doctor/{doctor_id}/")
    suspend fun getUserInfo(
        @Path("doctor_id") doctor_id: Int,
    ): Response<UserResponseDTO>

}