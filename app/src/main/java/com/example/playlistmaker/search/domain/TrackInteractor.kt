package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.TrackDto

interface TrackInteractor {
    fun searchSongs(expression: String, consumer: SearchConsumer)
    fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    fun getHistory(): ArrayList<TrackDto>
}