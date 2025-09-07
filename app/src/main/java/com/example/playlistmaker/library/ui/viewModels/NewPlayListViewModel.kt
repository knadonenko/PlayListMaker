package com.example.playlistmaker.library.ui.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants.TWO_SECONDS_DEBOUNCE_DELAY
import com.example.playlistmaker.library.domain.NewPlaylistInteractor
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class NewPlayListViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    var isClickable = true
    private val clickDebounce =
        debounce<Boolean>(TWO_SECONDS_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    fun onBtnClick() {
        isClickable = false
        clickDebounce(true)
    }


    fun createPlaylist(
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            interactor.createPlaylist(name, description, imageUri)
            onResultListener()
        }
    }

    fun updatePlaylist(
        playListId: Int,
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            interactor.updatePlaylist(playListId, name, description, imageUri)
            onResultListener()
        }
    }

}