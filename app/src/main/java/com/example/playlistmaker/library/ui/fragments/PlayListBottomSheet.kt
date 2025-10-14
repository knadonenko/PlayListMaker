package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetBinding
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.library.ui.states.BottomSheetPLState
import com.example.playlistmaker.library.ui.viewModels.BottomSheetViewModel
import com.example.playlistmaker.library.ui.viewholders.PlaylistViewHolder
import com.example.playlistmaker.search.data.TrackDto
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListBottomSheet(val track: TrackDto) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val viewModel by viewModel<BottomSheetViewModel>()

    private val playlistsAdapter = object : PlaylistAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
            return PlaylistViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.bottom_sheet_item, parent, false)
            )
        }
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecycler.adapter = playlistsAdapter

        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            when (it) {
                is BottomSheetPLState.Empty -> binding.playlistsRecycler.visibility = View.GONE
                is BottomSheetPLState.Playlists -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists.toMutableList()
                    binding.playlistsRecycler.visibility = View.VISIBLE
                }
                is BottomSheetPLState.AddTrackResult -> {
                    if (it.isAdded) {
                        showToast(getString(R.string.added, it.playlistName))
                        dismiss()
                    } else {
                        showToast(getString(R.string.already_added, it.playlistName))
                    }
                }
            }
        }

        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.bottomSheet_to_newPlaylist
            )
        }

    }

    private fun clickOnPlaylist(playlist: Playlist) {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClicked()
        viewModel.addTrackToPlaylist(track, playlist)
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

    companion object {
        const val TAG = "PlaylistsBottomSheet"

        fun newInstance(track: TrackDto): PlayListBottomSheet {
            return PlayListBottomSheet(track)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}