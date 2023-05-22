package ru.artbez.composeroulette.utils

import androidx.preference.PreferenceManager

class SharedPreferences {

    companion object{
        private val SharedPref = PreferenceManager.getDefaultSharedPreferences(MAIN)
        private const val sharedPrefKey = "recordKey"

        fun setRecord(gameRecord: String) = SharedPref.edit().putString(sharedPrefKey, gameRecord).apply()

        fun getRecord() = SharedPref.getString(sharedPrefKey, "")

        fun firstRecord() = if (SharedPref.contains(sharedPrefKey)) getRecord()!!.toInt() else 1000
    }
}