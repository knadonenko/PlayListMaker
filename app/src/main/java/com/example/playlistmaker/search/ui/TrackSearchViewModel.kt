package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.playlistmaker.util.Creator

class TrackSearchViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val moviesInteractor = Creator.provideTrackInteractor(getApplication())
    private val screenState = MutableLiveData<SearchScreenState>()
    private val showToast = SingleLiveEvent<String>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private val trackIsClickable = MutableLiveData(true)
    var trackState: LiveData<Boolean> = trackIsClickable

    private var latestSearchText: String? = null

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { getTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
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

}