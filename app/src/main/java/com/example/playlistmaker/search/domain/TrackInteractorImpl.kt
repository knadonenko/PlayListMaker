package com.example.playlistmaker.search.domain

import android.util.Log
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private var currentTrack: TrackDto? = null

    override fun searchSongs(expression: String) : Flow<Pair<List<TrackDto>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            Log.d("RESULT", result.toString())
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun addTrackToHistory(track: TrackDto) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(): ArrayList<TrackDto> {
        return repository.getHistory()
    }

    override fun setCurrentTrack(track: TrackDto) {
        currentTrack = track
    }

    override fun getCurrentTrack(): TrackDto {
        return currentTrack!!
    }
}