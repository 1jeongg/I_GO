package com.igoapp.i_go.feature_note.firebase

import android.R
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.igoapp.i_go.MainActivity
import com.igoapp.i_go.feature_note.data.storage.dataStore
import com.igoapp.i_go.feature_note.domain.util.log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TechFirebaseMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()){
            "Message Data - x     ${message.data["x"]}".log()
            "Message Data - y     ${message.data["y"]}".log()
            "Message Data - id    ${message.data["id"]}".log()
            "Message Data - image ${message.data["image"]}".log()
            "Message Data - name  ${message.data["name"]}".log()

            val X_FCM = getSharedPreferences("X_FCM", Context.MODE_PRIVATE)
            var editor = X_FCM.edit()
            editor.putString("X_FCM",message.data["x"])
            editor.apply()

            val Y_FCM = getSharedPreferences("Y_FCM", Context.MODE_PRIVATE)
            editor = Y_FCM.edit()
            editor.putString("Y_FCM",message.data["y"])
            editor.apply()

            val ID_FCM = getSharedPreferences("ID_FCM", Context.MODE_PRIVATE)
            editor = ID_FCM.edit()
            editor.putString("ID_FCM",message.data["id"])
            editor.apply()

            val IMAGE_FCM = getSharedPreferences("IMAGE_FCM", Context.MODE_PRIVATE)
            editor = IMAGE_FCM.edit()
            editor.putString("IMAGE_FCM",message.data["image"])
            editor.apply()

            val NAME_FCM = getSharedPreferences("NAME_FCM", Context.MODE_PRIVATE)
            editor = NAME_FCM.edit()
            editor.putString("NAME_FCM", if (message.data["name"]!!.isNotEmpty()) message.data["name"] else "안녕" )
            editor.apply()
        }

        if (message.notification != null) {
            "작업표시줄 내용: ${message.notification!!.title} ${message.notification!!.body}".log()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                "Fetching FCM registration token failed ${task.exception}".log()
                return@OnCompleteListener
            }

            val token = task.result
            "onMessageReceived ${token}".log()

        }
        )
    }


    override fun onNewToken(token: String) {
        val FCM_TOKEN = getSharedPreferences("FCM_TOKEN", Context.MODE_PRIVATE)
        var editor = FCM_TOKEN.edit()
        editor.putString("FCM_TOKEN",token)
        editor.apply()

        GlobalScope.launch{
            saveGCMToken(token)
        }
    }

    private suspend fun saveGCMToken(token: String) {
        "saveGCMToken ${token}".log()
        val gckTokenKey = stringPreferencesKey("gcm_token")
        baseContext.dataStore.edit { pref ->
            pref[gckTokenKey] = token
        }
    }
}
