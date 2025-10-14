package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.playlistmaker.library.ui.states.TrackLibraryState
import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.uicomponents.ErrorView
import com.example.playlistmaker.uicomponents.TrackItem
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
                FavoritesScreen(viewModel)
            }
        }
    }

    @Composable
    private fun FavoritesScreen(viewModel: FavoritesViewModel) {
        val state by viewModel.observeContentState().observeAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.settings_main_color)),
            contentAlignment = Alignment.Center
        ) {

            when (state) {
                is TrackLibraryState.Empty -> {
                    ErrorView(
                        icon = painterResource(id = R.drawable.not_found),
                        text = stringResource(R.string.empty_library),
                        showRetry = false,
                        onRetry = {}
                    )
                }

                is TrackLibraryState.FavoritesTracks -> {
                    val trackList = (state as TrackLibraryState.FavoritesTracks).tracks
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp)
                    ) {
                        items(trackList) { track ->
                            TrackItem(
                                track = track,
                                onClick = { clickOnTrack(track) }
                            )
                        }
                    }
                }
            }

        }
    }

    private fun clickOnTrack(track: TrackDto) {
        if (!viewModel.isClickable) return
        viewModel.onTrackClick(track)
        findNavController().navigate(R.id.favorites_to_player_action)
    }

}