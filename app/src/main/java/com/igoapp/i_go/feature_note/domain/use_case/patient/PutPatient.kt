package com.igoapp.i_go.feature_note.domain.use_case.patient

import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.domain.repository.PatientRepository
import com.igoapp.i_go.feature_note.domain.util.Resource
import com.igoapp.i_go.feature_note.domain.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PutPatient @Inject constructor(
    private val repository: PatientRepository
) {
    operator fun invoke(doctor_id: Int, patient_id: Int, patientDTO: PatientDTO): Flow<Resource<PatientDTO>> = flow {
        "HIHI".log()
        try {
            emit(Resource.Loading())
            val r = repository.putPatient(doctor_id, patient_id, patientDTO)
            when(r.code()) {
                200 -> {
                    "patient put success".log()
                    emit(Resource.Success(r.body()!!))
                }
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