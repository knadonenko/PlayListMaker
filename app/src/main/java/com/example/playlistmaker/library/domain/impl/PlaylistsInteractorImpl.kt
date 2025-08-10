package com.example.playlistmaker.library.domain.impl

import android.net.Uri
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.search.data.TrackDto

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) = repository.createPlaylist(playlistName, playlistDescription, imageUri)

    override suspend fun addTrack(track: TrackDto, playlistId: Int) =
        repository.addTrack(track, playlistId)

    override suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean =
        repository.isTrackAlreadyExists(trackId, playlistId)

    override suspend fun getPlaylists(): List<Playlist> =
        repository.getPlaylists()

    override suspend fun getPlaylist(playlistId: Int): Playlist =
        repository.getPlaylist(playlistId)


    override suspend fun getPlaylistTracks(playlistId: Int): List<TrackDto> =
        repository.getPlaylistTracks(playlistId)

    override suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        repository.updatePlaylist(playlistId, playlistName, playlistDescription, imageUri)
    }

    override suspend fun deleteTrack(trackId: Int, playlistId: Int) =
        repository.deleteTrack(trackId, playlistId)

    override suspend fun deletePlaylist(playlist: Playlist) =
        repository.deletePlaylist(playlist)
}