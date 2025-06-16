package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.BottomSheetState
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val _contentFlow: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState.Empty)
    val contentFlow: StateFlow<BottomSheetState> = _contentFlow

    init {
        fillData()
    }

    fun onPlaylistClicked(playlist: Playlist, track: TrackDto) {
        if (interactor.isTrackAddedToPlaylist(playlist, track)) {
            _contentFlow.value = BottomSheetState.AddedAlready(playlist)
        } else {
            viewModelScope.launch {
                interactor.addTrackToPlaylist(playlist, track)
                _contentFlow.value = BottomSheetState.AddedNow(playlist)
            }
        }
    }

    private fun fillData() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = BottomSheetState.Empty
        } else {
            _contentFlow.value = BottomSheetState.Content(playlists)
        }
    }

}