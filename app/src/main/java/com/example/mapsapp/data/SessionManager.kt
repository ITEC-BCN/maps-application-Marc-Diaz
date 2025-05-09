package com.example.mapsapp.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "session_prefs")

object SessionManager {
    val EMAIL = stringPreferencesKey("email")
    val PASSWORD = stringPreferencesKey("password")

    suspend fun saveSession(context: Context, accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = accessToken
            prefs[PASSWORD] = refreshToken
        }
    }

    suspend fun getSession(context: Context): Pair<String?, String?> {
        val prefs = context.dataStore.data.first()
        return Pair(prefs[EMAIL], prefs[PASSWORD])
    }

    suspend fun clearSession(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
