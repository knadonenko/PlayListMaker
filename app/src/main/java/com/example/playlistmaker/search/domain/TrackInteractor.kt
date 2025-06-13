package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchSongs(expression: String) : Flow<Pair<List<TrackDto>?, String?>>
    fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    fun getHistory(): ArrayList<TrackDto>
    fun setCurrentTrack(track: TrackDto)
    fun getCurrentTrack(): TrackDto
}