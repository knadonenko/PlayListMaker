package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants.TWO_SECONDS_DEBOUNCE_DELAY
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.BottomSheetPLState
import com.example.playlistmaker.library.ui.states.BottomSheetState
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val playlistsStateLiveData = MutableLiveData<BottomSheetPLState>()

    fun observePlaylistsState(): LiveData<BottomSheetPLState> = playlistsStateLiveData

    var isClickable = true

    private val playlistClickDebounce =
        debounce<Boolean>(TWO_SECONDS_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    fun requestPlaylists() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()
            if (playlists.isEmpty()) {
                playlistsStateLiveData.postValue(BottomSheetPLState.Empty)
            } else {
                playlistsStateLiveData.postValue(BottomSheetPLState.Playlists(playlists))
            }
        }
    }

    fun addTrackToPlaylist(track: TrackDto, playlist: Playlist) {
        viewModelScope.launch {
            if (interactor.isTrackAlreadyExists(track.trackId, playlist.playlistId)) {
                playlistsStateLiveData.postValue(
                    BottomSheetPLState.AddTrackResult(false, playlistName = playlist.name)
                )
            } else {
                interactor.addTrack(track, playlist.playlistId)
                playlistsStateLiveData.postValue(
                    BottomSheetPLState.AddTrackResult(true, playlistName = playlist.name)
                )
            }
        }
    }

    fun onPlaylistClicked() {
        isClickable = false
        playlistClickDebounce(true)
    }

}