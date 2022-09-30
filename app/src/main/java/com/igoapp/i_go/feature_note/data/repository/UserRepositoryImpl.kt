package com.igoapp.i_go.feature_note.data.repository

import com.igoapp.i_go.feature_note.data.remote.UserAPI
import com.igoapp.i_go.feature_note.data.remote.requestDTO.LoginPasswordDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.SignInDTO
import com.igoapp.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDetailDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.SignInResponseDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.UserResponseDTO
import com.igoapp.i_go.feature_note.data.storage.IdStore
import com.igoapp.i_go.feature_note.data.storage.TokenStore
import com.igoapp.i_go.feature_note.domain.model.ID
import com.igoapp.i_go.feature_note.domain.model.Token
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val store: TokenStore,
    private val idStore: IdStore,
    private val api: UserAPI
) : UserRepository {
    override suspend fun doLogin(loginPasswordDTO: LoginPasswordDTO): Response<Token> {
        return api.login(loginPasswordDTO)
    }
    override suspend fun doSignIn(signInDTO: SignInDTO): Response<SignInResponseDTO> {
        return api.signIn(signInDTO)
    }

    override suspend fun getUserInfo(doctor_id: Int): Response<UserResponseDTO> {
        return api.getUserInfo(doctor_id)
    }

    override suspend fun putUserInfo(
        doctor_id: Int,
        userDTO: UserDTO
    ): Response<UserDTO> {
        return api.putUserInfo(doctor_id, userDTO)
    }

    override fun getUserToken(): Token {
        return runBlocking(Dispatchers.IO) {
            store.getToken().first()
        }
    }

    override fun setUserToken(token: Token) {
        runBlocking(Dispatchers.IO) {
            store.setToken(token)
        }
    }
    override suspend fun getHospitals(): Response<List<HospitalDTO>> {
        return api.getHospitals()
    }
    override suspend fun getHospital(
        hospital_id: Int
    ): Response<HospitalDetailDTO> {
        return api.getHospital(hospital_id)
    }

    override fun getID(): ID {
        return runBlocking(Dispatchers.IO) {
            idStore.getId().first()
        }
    }

    override fun setID(id: ID) {
        runBlocking(Dispatchers.IO) {
            idStore.setId(id)
        }
    }
}