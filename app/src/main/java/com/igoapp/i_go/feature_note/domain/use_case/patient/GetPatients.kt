package com.igoapp.i_go.feature_note.domain.use_case.patient

import android.util.Log
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPatients @Inject constructor(
    private val repository: PatientRepository,
) {
    operator fun invoke(doctor_id: Int): Flow<Resource<List<PatientByIdDTO>>> = flow {

        try {
            emit(Resource.Loading())
            val r = repository.getPatients(doctor_id)
            when(r.code()) {
                200 -> {
                    Log.d("test", r.body()!!.toString())
                    emit(Resource.Success(r.body()!!))
                }
                else -> {
                    Log.d("test", "usecase ERROR ${r.code()}: ${r.errorBody().toString()}")
                }
            }

        } catch(e: HttpException) {
            Log.d("qwer", "An unexpected error occured")
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            Log.d("qwer", "Couldn't reach server. Check your internet connection.")
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}