package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.helpers.PlaceHolder.TRACKS_HISTORY
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.ui.components.AppBarComponent
import com.example.playlistmaker.uicomponents.ButtonView
import com.example.playlistmaker.uicomponents.ErrorView
import com.example.playlistmaker.uicomponents.LoadingView
import com.example.playlistmaker.uicomponents.TrackItem
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
                SearchScreen(searchViewModel)
            }
        }
    }

    @Composable
    private fun SearchScreen(viewModel: TrackSearchViewModel) {
        val state by viewModel.searchScreenState.observeAsState()
        var query by viewModel.query

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBarComponent(stringResource(R.string.search)) },
            containerColor = colorResource(R.color.settings_main_color)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SearchTextField(
                    query = query,
                    onQueryChange = { newQuery ->
                        query = newQuery
                        viewModel.searchDebounce(query)
                    })

                when (state) {
                    is SearchScreenState.Loading -> LoadingView()
                    is SearchScreenState.Success -> SearchContent(
                        trackList = (state as SearchScreenState.Success).tracks,
                        query = query,
                        onSearchClicked = ::clickOnTrack,
                        placeholder = SEARCH_RESULT
                    )

                    is SearchScreenState.ShowHistory -> SearchContent(
                        trackList = (state as SearchScreenState.ShowHistory).tracks,
                        query = query,
                        onSearchClicked = ::clickOnTrack,
                        placeholder = TRACKS_HISTORY,
                        onClearHistory = ::clearHistory
                    )
                }

            }
        }

    }

    @Composable
    fun SearchContent(
        trackList: List<TrackDto>,
        query: String,
        onSearchClicked: (TrackDto) -> Unit,
        placeholder: PlaceHolder,
        onClearHistory: () -> Unit = { }
    ) {
        if (trackList.isEmpty() && query.isNotBlank()) {
            ErrorView(
                icon = painterResource(R.drawable.not_found),
                text = stringResource(R.string.nothing_found),
                showRetry = false,
                onRetry = {},
                modifier = Modifier
                    .padding(top = 100.dp)
                    .fillMaxWidth()
            )
        }
        when (placeholder) {
            SEARCH_RESULT -> LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                items(trackList) { track ->
                    TrackItem(track = track, onClick = { onSearchClicked(track) })
                }
            }

            TRACKS_HISTORY -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.search_history),
                        color = colorResource(R.color.search_result_track_text_color),
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        fontSize = 18.sp,
                        maxLines = 1
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        items(trackList) { track ->
                            TrackItem(track = track, onClick = { onSearchClicked(track) })
                        }
                        item {
                            ButtonView(onClickAction = onClearHistory)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchTextField(query: String, onQueryChange: (String) -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, start = 16.dp, top = 8.dp, bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(
                    color = colorResource(R.color.black),
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontSize = 14.sp
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        color = colorResource(id = R.color.settings_icon_color),
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = colorResource(R.color.grey)
                    )
                },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { clearSearchForm() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.clear_search),
                            contentDescription = null,
                            tint = colorResource(R.color.grey)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.edit_text_color),
                    unfocusedTextColor = colorResource(R.color.edit_text_color),
                    focusedTextColor = colorResource(R.color.edit_text_color),
                    focusedIndicatorColor = colorResource(R.color.edit_text_color),
                    unfocusedContainerColor = colorResource(R.color.edit_text_color),
                    unfocusedIndicatorColor = colorResource(R.color.edit_text_color)
                )

            )
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