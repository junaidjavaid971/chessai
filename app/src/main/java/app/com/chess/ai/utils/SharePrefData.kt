package app.com.chess.ai.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharePrefData private constructor() {
    private var sp: SharedPreferences? = null
    private val spEditor: SharedPreferences.Editor? = null
    fun setContext(context: Context?) {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setPrefString(context: Context?, key: String?, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(key, value).apply()
    }

    fun setPrefInt(context: Context?, key: String?, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt(key, value).apply()
    }

    fun setPrefBoolean(context: Context?, key: String?, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(key, value).apply()
    }

    fun setPrefLong(context: Context?, key: String?, value: Long?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putLong(key, value!!).apply()
    }

    fun getPrefLong(context: Context?, key: String?): Long {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0L)
    }

    fun getPrefString(context: Context?, key: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "")
    }

    fun getPrefInt(context: Context?, key: String?): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0)
    }

    fun getPrefBoolean(context: Context?, key: String?): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
    }

    fun deletePrefData(context: Context?, key: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply()
    }

    fun containsPrefData(context: Context?, key: String?): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key)
    }

    companion object {
        @get:Synchronized
        val instance = SharePrefData()
    }
}