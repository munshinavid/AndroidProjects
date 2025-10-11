package com.example.ezy.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.ezy.EzyApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ezy_prefs")

object PrefsManager {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    private val dataStore = EzyApp.instance.dataStore

    suspend fun saveUserData(token: String, userId: Int, email: String, name: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USER_EMAIL_KEY] = email
            prefs[USER_NAME_KEY] = name
        }
    }

    val token: Flow<String?> = dataStore.data.map { prefs -> prefs[TOKEN_KEY] }
    val userId: Flow<Int?> = dataStore.data.map { prefs -> prefs[USER_ID_KEY] }
    val userEmail: Flow<String?> = dataStore.data.map { prefs -> prefs[USER_EMAIL_KEY] }
    val userName: Flow<String?> = dataStore.data.map { prefs -> prefs[USER_NAME_KEY] }

    suspend fun clearUserData() {
        dataStore.edit { it.clear() }
    }
}