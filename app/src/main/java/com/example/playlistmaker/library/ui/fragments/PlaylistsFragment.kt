package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.states.PlayListState
import com.example.playlistmaker.library.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistsAdapter = PlaylistAdapter {
        clickOnPlaylist()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.libraryFragment_to_newPlaylistFragment
            )
        }
    }

    private fun render(state: PlayListState) {
        when (state) {
            is PlayListState.Content -> showContent(state.playlists)
            PlayListState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            placeholderNoPlaylist.visibility = View.VISIBLE
            recyclerViewPlaylist.visibility = View.GONE
        }
    }

    private fun showContent(content: List<Playlist>) {

        binding.apply {
            placeholderNoPlaylist.visibility = View.GONE
            recyclerViewPlaylist.visibility = View.VISIBLE
        }

        playlistsAdapter.apply {
            playlists.clear()
            playlists.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylist.adapter = playlistsAdapter
        binding.recyclerViewPlaylist.addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
    }

    private fun clickOnPlaylist() {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClick()
        Toast
            .makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}