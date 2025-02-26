package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.helpers.SharedPrefsNames.HISTORY_TRACK
import com.example.playlistmaker.data.dto.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val preferences: SharedPreferences) {

    fun addHistory(track: Track) {
        val history = getHistory().apply {
            remove(track)
            add(0, track)
        }
        if (history.size > 10) history.removeLast()
        saveToHistory(history)
    }

    fun getHistory(): ArrayList<Track> {
        val json = preferences.getString(HISTORY_TRACK.prefName, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun clearHistory() {
        preferences.edit { remove(HISTORY_TRACK.prefName) }
    }

    private fun saveToHistory(history: MutableList<Track>) {
        val json = Gson().toJson(history)
        preferences.edit { putString(HISTORY_TRACK.prefName, json) }
    }

}