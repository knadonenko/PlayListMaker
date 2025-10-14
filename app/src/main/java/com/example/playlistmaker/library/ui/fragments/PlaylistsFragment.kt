package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.IntentConstants.PLAYLIST
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.states.PlayListsState
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.uicomponents.ButtonView
import com.example.playlistmaker.uicomponents.ErrorView
import com.example.playlistmaker.uicomponents.PlaylistCardView
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
                PlaylistScreen(viewModel)
            }
        }
    }

    @Composable
    private fun PlaylistScreen(viewModel: PlaylistsViewModel) {
        val state by viewModel.observeState().observeAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.settings_main_color)),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.settings_main_color)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                ButtonView(
                    onClickAction = { createNewPlaylist() },
                    stringResource(R.string.new_playlist)
                )
                Spacer(modifier = Modifier.height(16.dp))


                when (state) {
                    is PlayListsState.Empty -> {
                        ErrorView(
                            icon = painterResource(id = R.drawable.not_found),
                            text = stringResource(R.string.empty_playlists),
                            showRetry = false,
                            onRetry = {}
                        )
                    }

                    is PlayListsState.Content -> {
                        val playlists = (state as PlayListsState.Content).playlists
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(playlists) { playlist ->
                                PlaylistCardView(
                                    playlist = playlist,
                                    onClick = { clickOnPlaylist(playlist) }
                                )
                            }
                        }
                    }

                    else -> {}
                }
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