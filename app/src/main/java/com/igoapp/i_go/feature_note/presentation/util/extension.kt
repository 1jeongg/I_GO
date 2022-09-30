package com.igoapp.i_go.feature_note.presentation.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

suspend fun ExceptionMessage(
    value: String,
    message: String,
    scaffoldState: ScaffoldState
) {
    if (value.isEmpty()){
        scaffoldState.snackbarHostState.showSnackbar(
            message = message
        )
    }
}
suspend fun WrongPasswordExcept(
    PasswordValue: String,
    PasswordConfirmValue: String,
    scaffoldState: ScaffoldState
) {
    if (PasswordValue != PasswordConfirmValue){
        scaffoldState.snackbarHostState.showSnackbar(
            message = "패스워드가 틀렸습니다."
        )
    }
}