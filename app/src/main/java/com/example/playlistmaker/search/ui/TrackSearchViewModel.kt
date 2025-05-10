package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.SearchConsumer
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.util.debounce

class TrackSearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {

    private val screenState = MutableLiveData<SearchScreenState>()
    private val showToast = SingleLiveEvent<String>()
    private var lastQuery: String? = null
    private var isClickable = true
    private var latestSearchText: String? = null

    fun observeState(): LiveData<SearchScreenState> = screenState

    fun observeShowToast(): LiveData<String> = showToast

    private val trackSearchDebounce = debounce<String>(AppConstants.SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        getTracks(changedText)
    }

    fun searchDebounce(changedText: String? = lastQuery) {
        if (changedText.isNullOrEmpty()) {
            return
        }
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun onSearchClicked(track: TrackDto) {
        trackOnClickDebounce()
        addToHistory(track)
    }

    private fun trackOnClickDebounce() {
        isClickable = false
        debounce<Boolean>(AppConstants.SECOND_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }
    }

    fun getTracks(query: String? = lastQuery) {
        query?.let {
            screenState.postValue(SearchScreenState.Loading)
            trackInteractor.searchSongs(query, object : SearchConsumer {
                override fun consume(
                    foundTracks: List<TrackDto>?,
                    errorMessage: String?,
                    code: Int
                ) {
                    when (code) {
                        200 -> {
                            val tracks = arrayListOf<TrackDto>()
                            if (foundTracks!!.isNotEmpty()) {
                                tracks.addAll(foundTracks)

                                screenState.postValue(SearchScreenState.Success(tracks = tracks))
                            } else {
                                screenState.postValue(SearchScreenState.NothingFound)
                            }
                        }
                        else -> {
                            screenState.postValue(
                                SearchScreenState.Error(
                                    message = errorMessage!!
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun addToHistory(track: TrackDto) {
        trackInteractor.addTrackToHistory(track)
    }

    fun showHistory() {
        val historyTracks = getHistoryFromStorage()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(historyTracks))
        } else {
            renderState(SearchScreenState.Success(arrayListOf()))
        }
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
        screenState.postValue(SearchScreenState.Success(arrayListOf()))
    }

    private fun getHistoryFromStorage(): ArrayList<TrackDto> {
        return trackInteractor.getHistory()
    }

    private fun renderState(state: SearchScreenState) {
        screenState.postValue(state)
    }

}