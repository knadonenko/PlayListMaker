package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.library.data.Playlist

sealed class PlayListsState {
    data object Empty : PlayListsState()
    class Content(val playlists: List<Playlist>) : PlayListsState()
}