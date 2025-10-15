package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.ui.components.SearchScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel by viewModel<TrackSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(searchViewModel,
                    clickOnTrack = ::clickOnTrack,
                    clearHistory = ::clearHistory,
                    clearSearchForm = ::clearSearchForm)
            }
        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireActivity(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun clearSearchForm() {
        searchViewModel.clearSearch()
    }

    private fun clearHistory() {
        searchViewModel.clearHistory()
    }

    private fun clickOnTrack(track: TrackDto) {
        searchViewModel.onSearchClicked(track)
        findNavController().navigate(R.id.search_to_player_action)
    }

}