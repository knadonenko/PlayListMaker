package com.example.playlistmaker.search.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TrackSearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {

    private val screenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = screenState
    private val showToast = SingleLiveEvent<String>()
    private var isClickable = true
    private var searchJob: Job? = null

    var query = mutableStateOf("")
        private set

    fun observeShowToast(): LiveData<String> = showToast

    init {
        val history = showHistory()
        if (history.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(history))
        }
    }

    private val trackSearchDebounce =
        debounce<String>(
            AppConstants.TWO_SECONDS_DEBOUNCE_DELAY,
            viewModelScope,
            true
        ) { changedText ->
            searchJob?.cancel()
            getTracks(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (changedText.isNotEmpty()) {
            query.value = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun onSearchClicked(track: TrackDto) {
        trackOnClickDebounce()
        addToHistory(track)
        trackInteractor.setCurrentTrack(track)
    }

    private fun trackOnClickDebounce() {
        isClickable = false
        debounce<Boolean>(AppConstants.SECOND_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }
    }

    fun getTracks(query: String) {
        if (query.isNotEmpty() && query == this.query.value) {
            renderState(SearchScreenState.Loading)
            searchJob = viewModelScope.launch {
                trackInteractor.searchSongs(query).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(
        foundTracks: List<TrackDto>?,
        errorMessage: String?
    ) {
        val tracks = arrayListOf<TrackDto>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                renderState(SearchScreenState.Error(message = errorMessage))
                showToast.postValue(errorMessage!!)
            }

            tracks.isEmpty() -> {
                renderState(SearchScreenState.NothingFound)
            }

            else -> {
                renderState(SearchScreenState.Success(tracks = tracks))
            }
        }

    }

    private fun addToHistory(track: TrackDto) {
        trackInteractor.addTrackToHistory(track)
    }

    private fun showHistory(): List<TrackDto> {
        return trackInteractor.getHistory()
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
        screenState.postValue(SearchScreenState.Success(arrayListOf()))
    }

    private fun renderState(state: SearchScreenState) {
        screenState.postValue(state)
    }

    fun clearSearch() {
        query.value = ""
        val historyTracks = showHistory()
        searchJob?.cancel()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(historyTracks))
        } else {
            renderState(SearchScreenState.Success(listOf()))
        }
    }

}