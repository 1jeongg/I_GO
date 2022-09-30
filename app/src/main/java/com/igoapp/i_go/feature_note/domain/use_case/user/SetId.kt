package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.domain.model.ID
import com.igoapp.i_go.feature_note.domain.repository.UserRepository
import javax.inject.Inject

class SetId @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(id: ID) {
        repository.setID(id)
    }
}