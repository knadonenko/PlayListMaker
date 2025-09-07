package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants.TWO_SECONDS_DEBOUNCE_DELAY
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistMenuBottomSheetViewModel(private val playListsInteractor: PlaylistsInteractor) : ViewModel() {

    private var isClickable = true

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

    fun deletePlaylist(playlist: Playlist, onResultListener: () -> Unit) {
        viewModelScope.launch {
            playListsInteractor.deletePlaylist(playlist)
            onResultListener()
        }
    }

}