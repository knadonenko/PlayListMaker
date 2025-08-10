package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants.TWO_SECONDS_DEBOUNCE_DELAY
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.PlaylistState
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()

    var isClickable = true

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }

    fun requestPlayListInfo(playlistId: Int) {
        viewModelScope.launch {
            renderState(
                PlaylistState.PlaylistInfo(
                    playlistsInteractor.getPlaylist(playlistId)
                )
            )
            renderState(
                PlaylistState.PlaylistTracks(
                    playlistsInteractor.getPlaylistTracks(playlistId)
                )
            )
        }
    }

    fun deleteTrack(trackId: Int, playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrack(trackId, playlistId)
            renderState(
                PlaylistState.PlaylistTracks(
                    playlistsInteractor.getPlaylistTracks(playlistId)
                )
            )
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickable
        if (isClickable) {
            isClickable = false
            viewModelScope.launch {
                delay(TWO_SECONDS_DEBOUNCE_DELAY)
                isClickable = true
            }
        }
        return current
    }

}