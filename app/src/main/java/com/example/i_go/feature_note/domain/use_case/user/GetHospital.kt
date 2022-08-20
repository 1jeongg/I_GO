package com.example.i_go.feature_note.domain.use_case.user

import com.example.i_go.feature_note.data.remote.responseDTO.HospitalDetailDTO
import com.example.i_go.feature_note.domain.repository.UserRepository
import com.example.i_go.feature_note.domain.util.Resource
import com.example.i_go.feature_note.domain.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetHospital @Inject constructor(
    val repository: UserRepository
) {
    operator fun invoke(hospital_id: Int): Flow<Resource<HospitalDetailDTO>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.getHospital(hospital_id)
            when(r.code()) {
                200 -> emit(Resource.Success(r.body()!!))
                else -> r.errorBody().toString().log()
            }
        } catch(e: HttpException) {
            "An unexpected error occured".log()
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            "Couldn't reach server. Check your internet connection".log()
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}