package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.helpers.IntentConstants.PLAYLIST
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.library.ui.states.PlayListsState
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.library.ui.viewholders.PlaylistViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var _binding: FragmentPlaylistBinding
    private val playlistsAdapter = object : PlaylistAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            return PlaylistViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.playlist_item, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsState.Empty -> {
                    _binding.recyclerViewPlaylist.visibility = View.GONE
                    _binding.placeholderNoPlaylist.visibility = View.VISIBLE
                }

                is PlayListsState.Content -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists.toMutableList()
                    _binding.placeholderNoPlaylist.visibility = View.GONE
                    _binding.recyclerViewPlaylist.visibility = View.VISIBLE
                    _binding.recyclerViewPlaylist.smoothScrollToPosition(0)
                }
            }
        }

        _binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.libraryFragment_to_newPlaylistFragment
            )
        }

        initAdapter()
    }

    private fun initAdapter() {
        _binding.recyclerViewPlaylist.adapter = playlistsAdapter
        _binding.recyclerViewPlaylist.addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.list_to_single_playlist,
            Bundle().apply {
                putParcelable(PLAYLIST, playlist)
            }
        )
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

}