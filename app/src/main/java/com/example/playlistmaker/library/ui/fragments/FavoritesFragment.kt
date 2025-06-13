package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.ui.TrackLibraryState
import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.ui.adapter.SearchViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesViewModel>()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val tracksAdapter = SearchViewAdapter {
        clickOnTrack(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeContentState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.favoritesList.adapter = tracksAdapter
    }

    private fun clickOnTrack(track: TrackDto) {
        if (!viewModel.isClickable) return
        viewModel.onTrackClick(track)
        findNavController().navigate(R.id.favorites_to_player_action)
    }

    private fun render(state: TrackLibraryState) {
        when (state) {
            is TrackLibraryState.Empty -> {
                binding.favoritesList.visibility = View.GONE
                binding.nothingFound.visibility = View.VISIBLE
            }

            is TrackLibraryState.FavoritesTracks -> {
                tracksAdapter.tracks = state.tracks
                binding.nothingFound.visibility = View.GONE
                binding.favoritesList.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}