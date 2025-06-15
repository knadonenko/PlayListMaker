package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.library.data.Playlist

sealed class BottomSheetState(val content: List<Playlist> = emptyList()) {
    data object Empty : BottomSheetState()
    class AddedNow(val playlistModel: Playlist) : BottomSheetState()
    class AddedAlready(val playlistModel: Playlist) : BottomSheetState()
    class Content(playlists: List<Playlist>) : BottomSheetState(content = playlists)
}