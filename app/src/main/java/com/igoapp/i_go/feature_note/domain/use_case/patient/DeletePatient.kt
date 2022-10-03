package com.igoapp.i_go.feature_note.domain.use_case.patient

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientMessageDTO
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeletePatient @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(doctor_id: Int, patient_id: Int): Flow<Resource<PatientMessageDTO>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.deletePatient(doctor_id, patient_id)
            when(r.code()) {
                200 -> emit(Resource.Success(r.body()!!))
                else -> emit(Resource.Error("[DeletePatient] Error caused."))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}