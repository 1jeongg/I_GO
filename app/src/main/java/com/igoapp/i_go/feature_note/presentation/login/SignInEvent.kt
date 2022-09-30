package com.igoapp.i_go.feature_note.presentation.login

sealed class SignInEvent {
    data class EnteredUsername(val value: String): SignInEvent()
    data class EnteredPassword(val value: String): SignInEvent()
    data class EnteredPassword2(val value: String): SignInEvent()
    data class EnteredEmail(val value: String): SignInEvent()

    object SignIn: SignInEvent()
}