package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.IntentConstants.PLAYLIST
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.components.PlaylistScreen
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistScreen(viewModel,
                    ::createNewPlaylist,
                    ::clickOnPlaylist)
            }
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.list_to_single_playlist,
            Bundle().apply {
                putParcelable(PLAYLIST, playlist)
            }
        )
    }

    private fun createNewPlaylist() {
        findNavController().navigate(
            R.id.libraryFragment_to_newPlaylistFragment
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

}