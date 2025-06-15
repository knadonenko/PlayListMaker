package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.PlayListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor): ViewModel() {

    private val _contentFlow: MutableStateFlow<PlayListState> =
        MutableStateFlow(PlayListState.Empty)
    val contentFlow: StateFlow<PlayListState> = _contentFlow

    var isClickable = true

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch {
            interactor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }

    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = (PlayListState.Empty)
        } else {
            _contentFlow.value = (PlayListState.Content(playlists))
        }
    }

    fun onPlaylistClick() {
        isClickable = false
    }
}