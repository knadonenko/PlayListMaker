package com.example.playlistmaker.search.data

interface LocalStorage {
    fun addToHistory(track: TrackDto)
    fun clearHistory()
    fun getHistory(): ArrayList<TrackDto>
}