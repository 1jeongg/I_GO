package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.domain.model.Token
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetToken @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<Token> = flow {
        try {
            emit(repository.getUserToken())
        } catch(e: Exception) {
            emit(Token("", 0))
        }
    }
}