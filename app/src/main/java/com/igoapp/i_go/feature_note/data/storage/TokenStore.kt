package com.igoapp.i_go.feature_note.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.igoapp.i_go.feature_note.domain.model.Token
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore("user")

class TokenStore @Inject constructor(@ApplicationContext context: Context) {
    private val store = context.dataStore

    suspend fun setToken(token: Token) {
        store.edit {
            it[USER_TOKEN] = token.value
            it[USER_ID] = token.id.toString()
        }
    }

    fun getToken(): Flow<Token> {
        return store.data.map {
            Token(it[USER_TOKEN]!!)
            Token(it[USER_ID]!!)
        }
    }

    companion object {
        private val USER_TOKEN = stringPreferencesKey("user")
        private val USER_ID = stringPreferencesKey("id")
    }
}