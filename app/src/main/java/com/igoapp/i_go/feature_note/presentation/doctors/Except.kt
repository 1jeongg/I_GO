package com.igoapp.i_go.feature_note.presentation.doctors

import androidx.compose.material.ScaffoldState

suspend fun NameExcept(
    nameValue: String,
    scaffoldState: ScaffoldState
) {
    if (nameValue.isEmpty()){
        scaffoldState.snackbarHostState.showSnackbar(
            message = "이름을 입력해주세요."
        )
    }
}

suspend fun MajorExcept(
    majorValue: String,
    scaffoldState: ScaffoldState
) {
    if (majorValue.isEmpty()){
        scaffoldState.snackbarHostState.showSnackbar(
            message = "전공을 입력해주세요."
        )
    }
}

suspend fun FacilityExcept(
    facilityValue: String,
    scaffoldState: ScaffoldState
) {
    if (facilityValue.isEmpty()){
        scaffoldState.snackbarHostState.showSnackbar(
            message = "시설 이름을 입력해주세요."
        )
    }
}