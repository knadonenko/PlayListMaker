package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.components.FavoritesScreen
import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.search.data.TrackDto
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FavoritesScreen(viewModel, ::clickOnTrack)
            }
        }
    }

    private fun clickOnTrack(track: TrackDto) {
        if (!viewModel.isClickable) return
        viewModel.onTrackClick(track)
        findNavController().navigate(R.id.favorites_to_player_action)
    }

}