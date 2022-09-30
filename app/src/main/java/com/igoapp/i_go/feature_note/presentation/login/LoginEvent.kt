package com.igoapp.i_go.feature_note.presentation.login

sealed class LoginEvent {
    data class EnteredName(val value: String): LoginEvent()
    data class EnteredPassword(val value: String): LoginEvent()

    object Login: LoginEvent()
}