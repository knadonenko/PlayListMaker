package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.SearchConsumer

class TrackSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTrackInteractor(getApplication())
    private val screenState = MutableLiveData<SearchScreenState>()
    private val showToast = SingleLiveEvent<String>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private val isClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = isClickable

    fun observeState(): LiveData<SearchScreenState> = screenState

    fun observeShowToast(): LiveData<String> = showToast

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { getTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + AppConstants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchDebounce(changedText: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (!changedText.isNullOrEmpty()) {
            if ((lastQuery == changedText)) {
                return
            }
            this.lastQuery = changedText
            makeDelaySearching(changedText)
        }
    }

    fun onSearchClicked(track: TrackDto) {
        trackOnClickDebounce()
        addToHistory(track)
    }

    private fun trackOnClickDebounce() {
        isClickable.value = false
        handler.postDelayed({ isClickable.value = true }, AppConstants.SECOND_DEBOUNCE_DELAY)
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun getTracks(query: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        query?.let {
            screenState.postValue(SearchScreenState.Loading)
            tracksInteractor.searchSongs(query, object : SearchConsumer {
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
                                    message = getApplication<Application>()
                                        .getString(R.string.check_internet_connection),
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun addToHistory(track: TrackDto) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun clearSearch() {
        val historyTracks = showHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(historyTracks))
        } else {
            renderState(SearchScreenState.Success(arrayListOf()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        screenState.postValue(SearchScreenState.Success(arrayListOf()))

    }

    private fun showHistory(): ArrayList<TrackDto> {
        return tracksInteractor.getHistory()
    }

    private fun renderState(state: SearchScreenState) {
        screenState.postValue(state)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}