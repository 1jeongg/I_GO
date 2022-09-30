package com.igoapp.i_go.feature_note.domain.use_case.notification

import com.igoapp.i_go.feature_note.domain.model.Notification
import com.igoapp.i_go.feature_note.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotifications(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return repository.getNotifications().map {
            it.sortedByDescending { it.id }
        }
    }
}