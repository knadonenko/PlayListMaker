package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.ERROR
import com.example.playlistmaker.helpers.PlaceHolder.LOADING
import com.example.playlistmaker.helpers.PlaceHolder.NOT_FOUND
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.helpers.PlaceHolder.TRACKS_HISTORY
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.navigation.Router
import com.example.playlistmaker.search.ui.adapter.SearchViewAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: TrackSearchViewModel
    private lateinit var router: Router

    private val historyAdapter = SearchViewAdapter {
        clickOnTrack(it)
    }

    private val searchAdapter = SearchViewAdapter {
        clickOnTrack(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchViewModel = ViewModelProvider(this)[TrackSearchViewModel::class.java]

        searchViewModel.apply {

            observeState().observe(this@SearchActivity) {
                render(it)
            }

            observeShowToast().observe(this@SearchActivity) {
                showToast(it)
            }

        }

        binding.searchToolbar.setNavigationOnClickListener { router.goBack() }

        // search
        binding.recyclerView.adapter = searchAdapter

        binding.retryButton.setOnClickListener {
            searchViewModel.getTracks()
        }

        // history
        binding.recyclerViewHistory.adapter = historyAdapter
        binding.buttonClearHistory.setOnClickListener {
            searchViewModel.clearHistory()
        }

        //search input
        binding.inputSearchForm.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.clearForm.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            if (binding.inputSearchForm.hasFocus() && s.toString().isNotEmpty()) {
                showPlaceholder(SEARCH_RESULT)
            }
            searchViewModel.searchDebounce(binding.inputSearchForm.text.toString())
        }

        binding.inputSearchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.getTracks(binding.inputSearchForm.text.toString())
            }
            false
        }

        binding.retryButton.setOnClickListener {
            searchViewModel.getTracks(binding.inputSearchForm.text.toString())
        }

        binding.clearForm.visibility =
            if(binding.inputSearchForm.text.isNullOrEmpty()) View.GONE else View.VISIBLE

        binding.inputSearchForm.requestFocus()

        binding.clearForm.setOnClickListener {
            clearSearchForm()
        }

        router = Router(this)

    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Success -> {
                searchAdapter.tracks = state.tracks
                showPlaceholder(SEARCH_RESULT)
            }
            is SearchScreenState.ShowHistory -> {
                historyAdapter.tracks = state.tracks
                showPlaceholder(TRACKS_HISTORY)
            }
            is SearchScreenState.Error -> showPlaceholder(ERROR)
            is SearchScreenState.NothingFound -> showPlaceholder(NOT_FOUND)
            is SearchScreenState.Loading -> showPlaceholder(LOADING)
        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun clearSearchForm() {
        searchAdapter.tracks = arrayListOf()
        binding.inputSearchForm.setText("")
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        searchViewModel.clearSearch()
    }

    private fun clickOnTrack(track: TrackDto) {
        if (searchViewModel.trackIsClickable.value == false) return
        searchViewModel.onSearchClicked(track)
        router.openAudioPlayer(track)
    }

    private fun showPlaceholder(placeholder: PlaceHolder) {
        when (placeholder) {
            NOT_FOUND -> {
                binding.recyclerView.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.VISIBLE
                binding.placeholderCommunication.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
            }
            ERROR -> {
                binding.recyclerView.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderCommunication.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            }
            SEARCH_RESULT -> {
                binding.recyclerView.visibility = View.VISIBLE
                binding.historyList.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderCommunication.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
            }
            TRACKS_HISTORY -> {
                binding.recyclerView.visibility = View.GONE
                binding.historyList.visibility = View.VISIBLE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderCommunication.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
            }
            LOADING -> {
                binding.recyclerView.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderCommunication.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH, binding.inputSearchForm.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputSearchForm.setText(savedInstanceState.getString(SAVED_SEARCH, ""))
    }

    companion object {
        const val SAVED_SEARCH = "SAVED_SEARCH"
    }

}