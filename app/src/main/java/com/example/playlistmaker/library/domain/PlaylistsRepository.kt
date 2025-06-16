package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.data.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updateTracks(playlist: Playlist)
    fun getSavedPlaylists(): Flow<List<Playlist>>
}