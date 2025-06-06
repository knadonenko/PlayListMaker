package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun saveTrack(track: TrackDto)
    suspend fun deleteTrack(trackId: Int)
    fun getFavoritesTracks(): Flow<ArrayList<TrackDto>>
    fun isFavorite(trackId: Int): Flow<Boolean>
}