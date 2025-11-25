package br.edu.ufam.nutrilogapp.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

object AuthKeys {
    val TOKEN = stringPreferencesKey("auth_token")
    val CSRF_TOKEN = stringPreferencesKey("csrf_token")
    val SESSION_ID = stringPreferencesKey("session_id")
}

class AuthManager(private val context: Context) {
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AuthKeys.TOKEN]
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AuthKeys.TOKEN] = token
        }
    }

    suspend fun saveCookies(csrfToken: String, sessionId: String) {
        context.dataStore.edit { preferences ->
            preferences[AuthKeys.CSRF_TOKEN] = csrfToken
            preferences[AuthKeys.SESSION_ID] = sessionId
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AuthKeys.TOKEN)
            preferences.remove(AuthKeys.CSRF_TOKEN)
            preferences.remove(AuthKeys.SESSION_ID)
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[AuthKeys.TOKEN]
    }

    suspend fun getCsrfToken(): String? {
        return context.dataStore.data.first()[AuthKeys.CSRF_TOKEN]
    }

    suspend fun getSessionId(): String? {
        return context.dataStore.data.first()[AuthKeys.SESSION_ID]
    }
}

