package com.example.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.ERROR
import com.example.playlistmaker.helpers.PlaceHolder.LOADING
import com.example.playlistmaker.helpers.PlaceHolder.NOT_FOUND
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.helpers.PlaceHolder.TRACKS_HISTORY
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.ui.adapter.SearchViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModel<TrackSearchViewModel>()

    private val historyAdapter = SearchViewAdapter {
        clickOnTrack(it)
    }

    private val searchAdapter = SearchViewAdapter {
        clickOnTrack(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.apply {

            observeState().observe(viewLifecycleOwner) {
                render(it)
            }

            observeShowToast().observe(viewLifecycleOwner) {
                showToast(it)
            }

        }

        // search
        binding.recyclerView.adapter = searchAdapter

        binding.retryButton.setOnClickListener {
            searchViewModel.getTracks()
        }

        //search input
        binding.inputSearchForm.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.clearForm.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
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
            if (binding.inputSearchForm.text.isNullOrEmpty()) View.GONE else View.VISIBLE

        binding.inputSearchForm.requestFocus()

        binding.clearForm.setOnClickListener {
            clearSearchForm()
        }

        // history
        binding.recyclerViewHistory.adapter = historyAdapter
        binding.buttonClearHistory.setOnClickListener {
            searchViewModel.clearHistory()
        }

        searchViewModel.showHistory()
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
        Toast.makeText(requireActivity(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun clearSearchForm() {
        searchAdapter.tracks = arrayListOf()
        binding.inputSearchForm.setText("")
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        searchViewModel.showHistory()
    }

    private fun clickOnTrack(track: TrackDto) {
        searchViewModel.onSearchClicked(track)
        findNavController().navigate(R.id.search_to_player_action)
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH, binding.inputSearchForm.toString())
    }

    companion object {
        const val SAVED_SEARCH = "SAVED_SEARCH"
    }

}