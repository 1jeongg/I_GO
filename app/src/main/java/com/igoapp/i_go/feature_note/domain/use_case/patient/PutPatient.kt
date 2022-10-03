package com.igoapp.i_go.feature_note.domain.use_case.patient

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PutPatient @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(doctor_id: Int, patient_id: Int, patientDTO: PatientDTO): Flow<Resource<PatientDTO>> = flow {
        try {
            emit(Resource.Loading())
            val r = repository.putPatient(doctor_id, patient_id, patientDTO)
            when(r.code()) {
                200  -> emit(Resource.Success(r.body()!!))
                else -> emit(Resource.Error("[PutPatient] Error caused."))
            }
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}