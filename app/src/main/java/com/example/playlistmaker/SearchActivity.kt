package com.example.playlistmaker

import android.content.Context
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.SearchViewAdapter
import com.example.playlistmaker.api.RetrofitHelper.retrofit
import com.example.playlistmaker.api.SearchAPI
import com.example.playlistmaker.api.SearchResponse
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.ERROR
import com.example.playlistmaker.helpers.PlaceHolder.NOT_FOUND
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var clearSearchButton: ImageView
    private lateinit var searchInput: EditText
    private var searchQuery = ""
    private val tracks = ArrayList<Track>()
    private val serviceSearch = retrofit.create(SearchAPI::class.java)
    private lateinit var searchResultRv: RecyclerView
    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: Button

    companion object {
        const val SAVED_SEARCH = "SAVED_SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<Toolbar>(R.id.search_toolbar).setNavigationOnClickListener {
            finish()
        }

        placeholderNothingWasFound = findViewById(R.id.placeholderNotFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunication)

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
                true
            }
            false
        }

        searchResultRv = findViewById(R.id.recycler_view)
        searchResultRv.adapter = SearchViewAdapter(tracks)

        buttonRetry = findViewById(R.id.retryButton)
        buttonRetry.setOnClickListener {
            getTrack()
        }

    }

    private fun getTrack() {
        serviceSearch.searchTrack(searchInput.text.toString())
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>,
                ) {
                    if (searchQuery.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == 200) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        searchResultRv.adapter?.notifyDataSetChanged()
                        showPlaceholder(SEARCH_RESULT)
                    } else {
                        showPlaceholder(NOT_FOUND)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showPlaceholder(ERROR)
                }
            })
    }

    private val inputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearSearchButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            searchQuery = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun clearSearchForm() {
        searchInput.setText("")

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        searchInput.setText("")
        inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)

        placeholderNothingWasFound.isVisible = false
        placeholderNothingWasFound.isVisible = false
        tracks.clear()
        searchResultRv.adapter?.notifyDataSetChanged()
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
            else -> {
                searchResultRv.visibility = View.VISIBLE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
        }
    }

}