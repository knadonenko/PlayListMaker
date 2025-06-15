package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.data.Playlist

interface NewPlaylistInteractor {
    suspend fun create(playlist: Playlist)
}