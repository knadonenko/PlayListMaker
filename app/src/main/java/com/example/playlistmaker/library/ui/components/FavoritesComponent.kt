package com.example.playlistmaker.library.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.states.TrackLibraryState
import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.uicomponents.ErrorView
import com.example.playlistmaker.uicomponents.TrackItemComponent

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onTrackClick: (TrackDto) -> Unit
) {
    val state by viewModel.observeContentState().observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.settings_main_color)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (state) {
            is TrackLibraryState.Empty -> {
                Spacer(modifier = Modifier.height(100.dp))
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
                        TrackItemComponent(
                            track = track,
                            onClick = { onTrackClick(track) }
                        )
                    }
                }
            }
        }

    }
}