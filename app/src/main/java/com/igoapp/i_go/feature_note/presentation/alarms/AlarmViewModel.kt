package com.igoapp.i_go.feature_note.presentation.alarms

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igoapp.i_go.feature_note.domain.model.Notification
import com.igoapp.i_go.feature_note.domain.use_case.notification.NotificationUseCases
import com.igoapp.i_go.feature_note.domain.use_case.patient.PatientUseCases
import com.igoapp.i_go.feature_note.domain.use_case.user.UserUseCases
import com.igoapp.i_go.feature_note.domain.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases,
    private val patientUseCases: PatientUseCases
): ViewModel() {

    private val _state = mutableStateOf(AlarmState())
    val state: State<AlarmState> = _state

    private var getNotificationJob: Job? = null

    init {
        getNotifications()
    }

    private fun getNotifications() {
        getNotificationJob?.cancel()
        getNotificationJob = notificationUseCases.getNotifications()
            .onEach { notications ->
                _state.value = state.value.copy(
                    notifications = notications,
                )
            }
            .launchIn(viewModelScope)
    }

    fun addNotification(
        patient_id: Int,
        patient_x: Double,
        patient_y: Double,
        patient_image: Int,
        patient_name: String
    ){
        viewModelScope.launch {
            try {
                notificationUseCases.addNotification(
                    Notification(
                        patient_id = patient_id,
                        patient_x = patient_x,
                        patient_y = patient_y,
                        image = patient_image,
                        name = patient_name
                    )
                )
                "노트앱은 ${patient_id}".log()
            } catch(e: Exception) {
                "푸시 알림 저장 실패".log()
            }
        }
    }

    fun callPatients(patient_id: Int){
            try {
                "푸시 알림 호출 ㄱㄱ".log()
                patientUseCases.callPatient(patient_id = patient_id).launchIn(viewModelScope)
            } catch(e: Exception){
                "푸시 알림에서 호출 실패".log()
            }
    }

    fun deleteNotifications(notification: Notification){
        viewModelScope.launch {
            notificationUseCases.deleteNotification(notification)
        }
    }


}