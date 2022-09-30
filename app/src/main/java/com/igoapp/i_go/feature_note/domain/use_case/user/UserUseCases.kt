package com.igoapp.i_go.feature_note.domain.use_case.user

import com.igoapp.i_go.feature_note.domain.use_case.user.*
import javax.inject.Inject

data class UserUseCases @Inject constructor(
    val doLogin: DoLogin,
    val doSignIn: DoSignIn,
    val getUserInfo: GetUserInfo,
    val putUserInfo: PutUserInfo,
    val getToken: GetToken,
    val setToken: SetToken,
    val getHospitals: GetHospitals,
    val getHospital: GetHospital,
    val getId: GetId,
    val setId: SetId
)
