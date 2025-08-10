package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.search.data.TrackDto

sealed interface PlaylistState {

    data class PlaylistInfo(val playlist: Playlist) : PlaylistState
    data class PlaylistTracks(val tracks: List<TrackDto>) : PlaylistState
}