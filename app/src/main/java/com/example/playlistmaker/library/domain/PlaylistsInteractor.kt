package com.example.playlistmaker.library.domain

import android.net.Uri
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?)
    suspend fun addTrack(track: TrackDto, playlistId: Int)
    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean
    suspend fun deleteTrack(trackId: Int, playlistId: Int)
    suspend fun getPlaylist(playlistId: Int): Playlist
    suspend fun getPlaylistTracks(playlistId: Int): List<TrackDto>
    suspend fun getPlaylists(): List<Playlist>
    suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri?)
    suspend fun deletePlaylist(playlist: Playlist)
}