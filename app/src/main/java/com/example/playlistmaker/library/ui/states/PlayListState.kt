package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.library.data.Playlist

sealed class PlayListState {
    data object Empty : PlayListState()
    class Content(val playlists: List<Playlist>) : PlayListState()
}