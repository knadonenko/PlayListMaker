package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.PlayListsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val _contentFlow: MutableStateFlow<PlayListsState> =
        MutableStateFlow(PlayListsState.Empty)
    val contentFlow: StateFlow<PlayListsState> = _contentFlow

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()
            processResult(playlists)
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = (PlayListsState.Empty)
        } else {
            _contentFlow.value = (PlayListsState.Content(playlists))
        }
    }
}