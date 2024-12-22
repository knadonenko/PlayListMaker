package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.SearchViewAdapter
import com.example.playlistmaker.api.RetrofitHelper.retrofit
import com.example.playlistmaker.api.SearchAPI
import com.example.playlistmaker.api.SearchResponse
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.ERROR
import com.example.playlistmaker.helpers.PlaceHolder.NOT_FOUND
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.helpers.PlaceHolder.TRACKS_HISTORY
import com.example.playlistmaker.helpers.SharedPrefsNames.HISTORY_PREFS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var searchQuery = ""
    private lateinit var clearSearchButton: ImageView
    private lateinit var searchInput: EditText
    private val serviceSearch = retrofit.create(SearchAPI::class.java)
    private lateinit var searchResultRv: RecyclerView
    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: Button

    //history
    private lateinit var buttonClearHistory: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyList: LinearLayout
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var history: SharedPreferences

    companion object {
        const val SAVED_SEARCH = "SAVED_SEARCH"
    }

    private val historyAdapter = SearchViewAdapter {
        searchHistory.addHistory(it)
    }

    private val searchAdapter = SearchViewAdapter {
        searchHistory.addHistory(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<Toolbar>(R.id.search_toolbar).setNavigationOnClickListener {
            finish()
        }

        //placeholders
        placeholderNothingWasFound = findViewById(R.id.placeholderNotFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunication)

        // search
        clearSearchButton = findViewById(R.id.clear_form)
        clearSearchButton.visibility = View.GONE
        clearSearchButton.setOnClickListener {
            clearSearchForm()
        }

        searchInput = findViewById(R.id.input_search_form)
        searchInput.requestFocus()
        searchInput.addTextChangedListener(inputTextWatcher)
        searchInput.setText(searchQuery)
        searchInput.requestFocus()

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack()
            }
            false
        }

        searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchInput.text.isEmpty()) {
                showPlaceholder(SEARCH_RESULT)
            }
        }

        searchResultRv = findViewById(R.id.recycler_view)
        searchResultRv.adapter = searchAdapter

        buttonRetry = findViewById(R.id.retryButton)
        buttonRetry.setOnClickListener {
            getTrack()
        }

        // history
        buttonClearHistory = findViewById(R.id.button_clear_history)
        buttonClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            showPlaceholder(SEARCH_RESULT)
        }
        historyList = findViewById(R.id.history_list)
        recyclerHistory = findViewById(R.id.recyclerViewHistory)

        recyclerHistory.adapter = historyAdapter
        history = getSharedPreferences(HISTORY_PREFS.prefName, MODE_PRIVATE)
        searchHistory = SearchHistory(history)

        if (searchInput.text.isEmpty()) {
            historyAdapter.tracks = searchHistory.getHistory()
            if (historyAdapter.tracks.isNotEmpty()) {
                showPlaceholder(TRACKS_HISTORY)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SAVED_SEARCH, "")
        searchInput.setText(searchQuery)
    }

    private fun getTrack() {
        if (searchQuery.isNotEmpty()) {
            serviceSearch.searchTrack(searchQuery)
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>,
                    ) {
                        when (response.code()) {
                            200 -> {
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    searchAdapter.tracks = response.body()?.results!!
                                    showPlaceholder(SEARCH_RESULT)
                                } else {
                                    showPlaceholder(NOT_FOUND)
                                }
                            }
                            else -> {
                                showPlaceholder(ERROR)
                            }
                        }

                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        showPlaceholder(ERROR)
                    }
                })
        }
    }

    private val inputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearSearchButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            searchQuery = s.toString()

            if (searchInput.hasFocus() && searchQuery.isNotEmpty()) {
                showPlaceholder(SEARCH_RESULT)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun clearSearchForm() {
        searchInput.setText("")
        historyAdapter.tracks = searchHistory.getHistory()
        if (historyAdapter.tracks.isNotEmpty()) {
            showPlaceholder(TRACKS_HISTORY)
        } else {
            showPlaceholder(SEARCH_RESULT)
        }
        val view = this.currentFocus
        if (view != null) {
            val input = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showPlaceholder(placeholder: PlaceHolder) {

        when (placeholder) {
            NOT_FOUND -> {
                searchResultRv.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.VISIBLE
            }
            ERROR -> {
                searchResultRv.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.VISIBLE
            }
            SEARCH_RESULT -> {
                searchResultRv.visibility = View.VISIBLE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
            TRACKS_HISTORY -> {
                searchResultRv.visibility = View.GONE
                historyList.visibility = View.VISIBLE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
        }
    }

}