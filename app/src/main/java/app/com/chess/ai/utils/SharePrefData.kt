package app.com.chess.ai.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharePrefData {
    private var sp: SharedPreferences? = null
    fun setContext(context: Context?) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }
    private constructor() {}

    companion object {
        @get:Synchronized
        val instance = SharePrefData()
    }
}