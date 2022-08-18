package com.example.i_go.feature_note.domain.use_case

import com.example.i_go.feature_note.data.remote.requestDTO.SignInDTO
import com.example.i_go.feature_note.data.remote.responseDTO.SignInResponseDTO
import com.example.i_go.feature_note.domain.repository.UserRepository
import com.example.i_go.feature_note.domain.util.Resource
import com.example.i_go.feature_note.domain.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DoSignIn @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(signInDTO: SignInDTO): Flow<Resource<SignInResponseDTO>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.doSignIn(signInDTO)
            when(r.code()) {
                201 -> {
                    emit(Resource.Success(r.body()!!))
                }
                else -> {
                    emit(Resource.Error("회원가입 실패"))
                    "usecase ERROR ${r.code()}: ${r.errorBody().toString()}".log()
                }
            }

        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}