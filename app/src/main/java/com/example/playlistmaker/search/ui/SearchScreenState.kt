package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.data.TrackDto

interface SearchScreenState {
    object Loading : SearchScreenState
    object NothingFound : SearchScreenState
    data class Success(val tracks: List<TrackDto>) : SearchScreenState
    data class ShowHistory(val tracks: List<TrackDto>) : SearchScreenState
    data class Error(val message: String) : SearchScreenState
}