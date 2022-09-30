package com.igoapp.i_go.feature_note.domain.use_case.notification

data class NotificationUseCases(
    val getNotifications: GetNotifications,
    val addNotification: AddNotification,
    val deleteNotification: DeleteNotification
)
