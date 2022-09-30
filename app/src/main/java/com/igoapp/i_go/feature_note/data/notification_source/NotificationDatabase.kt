package com.igoapp.i_go.feature_note.data.notification_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igoapp.i_go.feature_note.domain.model.Notification

@Database(
    entities = [Notification::class],
    version = 1
)
abstract class NotificationDatabase: RoomDatabase() {

    abstract val notificationDao: NotificationDao

    companion object {
        const val DATABASE_NAME = "notifications_db"
    }
}