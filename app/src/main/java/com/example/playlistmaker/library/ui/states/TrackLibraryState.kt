package com.example.playlistmaker.library.ui.states

import com.example.playlistmaker.search.data.TrackDto

sealed interface TrackLibraryState {
    data object Empty : TrackLibraryState

    data class FavoritesTracks(
        val tracks: ArrayList<TrackDto>,
    ) : TrackLibraryState
}