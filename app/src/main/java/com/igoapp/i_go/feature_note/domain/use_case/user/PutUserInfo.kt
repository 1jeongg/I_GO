package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.data.remote.requestDTO.UserDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.UserResponseDTO
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PutUserInfo @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(doctor_id: Int, userDTO: UserDTO): Flow<Resource<UserDTO>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.putUserInfo(doctor_id, userDTO)
            when(r.code()) {
                200 -> {
                    emit(Resource.Success(r.body()!!))
                    "의료진 성공: ${r.body()}".log()
                }
                else -> {
                    emit(Resource.Error(("의료진 실패ㅠ")))
                    "usecase ERROR ${r.code()}: ${r.errorBody().toString()}".log()
                }
            }

        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}