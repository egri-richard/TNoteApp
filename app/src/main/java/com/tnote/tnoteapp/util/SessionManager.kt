package com.tnote.tnoteapp.util

import android.content.Context
import com.tnote.tnoteapp.R

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val CURRENT_NOTE = "current_note"
        const val CURRENT_TIMETABLE = "current_timetable"
    }

    fun clearPrefs() {
        return prefs.edit().clear().apply()
    }

    fun saveCredentials(token: String, id: Int) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID, 0)
    }

    fun getAuthToken(): String {
        val token = prefs.getString(USER_TOKEN, null)
        return "Bearer $token"
    }
}