package com.igoapp.i_go.feature_note.presentation.add_edit_patient

import androidx.compose.ui.focus.FocusState

sealed class AddEditPatientEvent {
    data class EnteredName(val value: String): AddEditPatientEvent()
    data class ChangeNameFocus(val focusState: FocusState): AddEditPatientEvent()
    data class EnteredSex(val value: String): AddEditPatientEvent()
    data class EnteredAge(val value: String): AddEditPatientEvent()
    data class ChangeAgeFocus(val focusState: FocusState): AddEditPatientEvent()
    data class EnteredBloodType(val value: String): AddEditPatientEvent()
    data class EnteredBloodRh(val value: String): AddEditPatientEvent()
    data class EnteredDiseases(val value: String): AddEditPatientEvent()
    data class ChangeDiseasesFocus(val focusState: FocusState): AddEditPatientEvent()
    data class EnteredExtra(val value: String): AddEditPatientEvent()
    data class ChangeExtraFocus(val focusState: FocusState): AddEditPatientEvent()

    data class ChangeImage(val image: Int): AddEditPatientEvent()
    object SavePatient: AddEditPatientEvent()
}