package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.library.data.Playlist

sealed interface BottomSheetPLState {
    data object Empty : BottomSheetPLState
    data class Playlists(val playlists: List<Playlist>) : BottomSheetPLState
    data class AddTrackResult(val isAdded: Boolean, val playlistName: String) : BottomSheetPLState
}