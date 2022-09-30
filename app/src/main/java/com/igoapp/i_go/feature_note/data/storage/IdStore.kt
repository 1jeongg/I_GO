package com.igoapp.i_go.feature_note.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.igoapp.i_go.feature_note.domain.model.ID
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.idStore by preferencesDataStore("id")

class IdStore @Inject constructor(@ApplicationContext context: Context) {
    private val store = context.idStore

    suspend fun setId(id: ID) {
        store.edit {
            it[USER_ID] = id.userId.toString()
            it[PATIENT_ID] = id.patientId.toString()
            it[HOSPITAL_ID] = id.hospitalId.toString()
        }
    }

    fun getId() : Flow<ID> {
        return store.data.map {
            ID(it[USER_ID]?.toInt()!!)
            ID(it[PATIENT_ID]?.toInt()!!)
            ID(it[HOSPITAL_ID]?.toInt()!!)
        }
    }

    companion object {
        private val USER_ID = stringPreferencesKey("user")
        private val PATIENT_ID = stringPreferencesKey("patient")
        private val HOSPITAL_ID = stringPreferencesKey("hospital")
    }
}