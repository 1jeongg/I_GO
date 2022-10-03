package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.domain.model.ID
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetId @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<ID> = flow {
        try {
            emit(repository.getID())
        } catch(e: Exception) {
            emit(ID(0,0, 0))
        }
    }
}