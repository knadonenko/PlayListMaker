package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.NewPlaylistInteractor
import com.example.playlistmaker.library.domain.PlaylistsRepository

class NewPlaylistInteractorImpl(private val repository: PlaylistsRepository) :
    NewPlaylistInteractor {
    override suspend fun create(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}