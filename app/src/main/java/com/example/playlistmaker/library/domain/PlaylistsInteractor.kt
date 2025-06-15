package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    fun isTrackAddedToPlaylist(playlist: Playlist, track: TrackDto): Boolean
    suspend fun addTrackToPlaylist(playlist: Playlist, track: TrackDto)
}