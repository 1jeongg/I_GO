package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.data.remote.requestDTO.LoginPasswordDTO
import com.igoapp.i_go.feature_note.domain.model.Token
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DoLogin @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(loginPasswordDTO: LoginPasswordDTO): Flow<Resource<Token>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.doLogin(loginPasswordDTO)
            when(r.code()) {
                200 -> {
                    "성공".log()
                    emit(Resource.Success(r.body()!!))
                }
                else -> {
                    emit(Resource.Error("login failed"))
                    "usecase ERROR ${r.code()}: ${r.errorBody().toString()}".log()
                }
            }

        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}