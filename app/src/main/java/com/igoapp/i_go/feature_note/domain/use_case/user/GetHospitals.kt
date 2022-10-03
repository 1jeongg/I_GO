package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.data.remote.responseDTO.HospitalDTO
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHospitals @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<List<HospitalDTO>>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.getHospitals()
            when(r.code()) {
                200 -> emit(Resource.Success(r.body()!!))
                else -> emit(Resource.Error("Error in Get hospitals"))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}