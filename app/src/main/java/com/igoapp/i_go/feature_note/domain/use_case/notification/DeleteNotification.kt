package com.igoapp.i_go.feature_note.domain.use_case.notification

import com.igoapp.i_go.feature_note.domain.model.Notification
import com.igoapp.i_go.feature_note.domain.repository.NotificationRepository

class DeleteNotification(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: Notification){
        repository.deleteNotification(notification)
    }
}