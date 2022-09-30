package com.igoapp.i_go.feature_note.domain.repository

import com.igoapp.i_go.feature_note.data.remote.requestDTO.LoginPasswordDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.SignInDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.*
import com.igoapp.i_go.feature_note.domain.model.ID
import com.igoapp.i_go.feature_note.domain.model.Token
import retrofit2.Response

interface UserRepository {
    suspend fun doLogin(emailPw: LoginPasswordDTO): Response<Token>
    suspend fun doSignIn(signInDTO: SignInDTO): Response<SignInResponseDTO>

    suspend fun getUserInfo(doctor_id: Int): Response<UserResponseDTO>
    suspend fun putUserInfo(doctor_id: Int, userDTO: UserDTO): Response<UserDTO>

    fun getUserToken(): Token
    fun setUserToken(token: Token)

    fun getID(): ID
    fun setID(id: ID)

    suspend fun getHospitals(): Response<List<HospitalDTO>>
    suspend fun getHospital(hospital_id: Int): Response<HospitalDetailDTO>

}