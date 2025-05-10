package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.helpers.AppConstants.MAX_TRACKS
import com.example.playlistmaker.helpers.SharedPrefsConstants.HISTORY_TRACK
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class TrackStorage(private val preferences: SharedPreferences) : LocalStorage {
    override fun addToHistory(track: TrackDto) {
        val searchedTracks = getHistory().map { TrackDto(it.trackId,
            it.trackName,
            it.artistName,
            it.trackTimeMillis,
            it.artworkUrl100,
            it.collectionName,
            it.releaseDate,
            it.primaryGenreName,
            it.country,
            it.previewUrl) } as MutableList
        if (searchedTracks.contains(TrackDto(track.trackId, track.trackName,
                track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
                track.releaseDate, track.primaryGenreName, track.country, track.previewUrl))) {
            searchedTracks.remove(track)
        }
        searchedTracks.add(0, track)
        if (searchedTracks.size > MAX_TRACKS) {
            searchedTracks.removeLast()
        }

        val json = Gson().toJson(searchedTracks)
        preferences.edit {
                putString(HISTORY_TRACK.prefName, json)
            }
    }

    override fun clearHistory() {
        preferences.edit {
                clear()
            }
    }

    override fun getHistory(): ArrayList<TrackDto> {
        val json =
            preferences.getString(HISTORY_TRACK.prefName, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<TrackDto>>() {}.type)
    }
}