package com.igoapp.i_go.feature_note.domain.repository

import com.igoapp.i_go.feature_note.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotifications(): Flow<List<Notification>>

    suspend fun insertNotification(notification: Notification)

    suspend fun deleteNotification(notification: Notification)
}