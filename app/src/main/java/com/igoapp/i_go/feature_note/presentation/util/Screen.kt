package com.igoapp.i_go.feature_note.presentation.util

sealed class Screen(val route: String) {
    object PatientsScreen: Screen("patients_screen")
    object AddEditPatientScreen: Screen("add_edit_patient_screen")
    object DoctorScreen: Screen("doctor_screen")
    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_page")
    object AlarmScreen: Screen("alarm_screen")
    object AgreeScreen: Screen("agree_screen")
}
