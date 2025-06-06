package com.example.playlistmaker.library.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.library.domain.LibraryInteractor
import com.example.playlistmaker.library.ui.TrackLibraryState
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class FavoritesViewModel(private val interactor: LibraryInteractor) : ViewModel() {

    private val contentStateLiveData = MutableLiveData<TrackLibraryState>()
    fun observeContentState(): LiveData<TrackLibraryState> = contentStateLiveData

    var isClickable = true

    private val trackClickDebounce = debounce<Boolean>(
        AppConstants.TWO_SECONDS_DEBOUNCE_DELAY,
        viewModelScope, false
    ) { isClickable = it }

    init {
        getFavoriteTracks()
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            interactor
                .getFavoriteTracks()
                .collect { favoritesTracks ->
                    processResult(favoritesTracks)
                }
        }
    }

    private fun processResult(trackList: ArrayList<TrackDto>) {
        when {
            trackList.isEmpty() -> {
                contentStateLiveData.value = TrackLibraryState.Empty
            }
            else -> {
                contentStateLiveData.value = TrackLibraryState.FavoritesTracks(trackList)
            }
        }
    }

    fun onTrackClick() {
        isClickable = false
        trackClickDebounce(true)
    }

}