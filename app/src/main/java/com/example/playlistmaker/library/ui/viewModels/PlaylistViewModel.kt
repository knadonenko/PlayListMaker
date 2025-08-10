package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.helpers.AppConstants.TWO_SECONDS_DEBOUNCE_DELAY
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.PlaylistState
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor,
    private val trackInteractor: TrackInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()

    private var isClickable = true

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
        debounce<Boolean>(AppConstants.SECOND_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }
        return current
    }

    fun clickOnTrack(track: TrackDto) {
        clickDebounce()
        trackInteractor.setCurrentTrack(track)
    }

}