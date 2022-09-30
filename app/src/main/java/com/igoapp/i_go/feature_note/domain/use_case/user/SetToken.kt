package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.domain.model.Token
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import javax.inject.Inject

class SetToken @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(token: Token) {
        repository.setUserToken(token)
    }
}