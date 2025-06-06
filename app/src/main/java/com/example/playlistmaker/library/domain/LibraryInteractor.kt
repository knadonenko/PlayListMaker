package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

interface LibraryInteractor {
    suspend fun saveTrack(track: TrackDto)
    suspend fun deleteTrack(trackId: Int)
    fun getFavoriteTracks(): Flow<ArrayList<TrackDto>>
    fun isFavorite(trackId: Int): Flow<Boolean>
}