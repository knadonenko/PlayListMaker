package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.ui.states.PlayListsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayListsState>()

    fun observeState(): LiveData<PlayListsState> = stateLiveData

    private fun renderState(state: PlayListsState) {
        stateLiveData.postValue(state)
    }

    fun requestPlaylists() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()

            if (playlists.isEmpty()) {
                renderState(PlayListsState.Empty)
            } else {
                renderState(PlayListsState.Content(playlists))
            }
        }
    }
}