package com.example.playlistmaker.search.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.PlaceHolder
import com.example.playlistmaker.helpers.PlaceHolder.SEARCH_RESULT
import com.example.playlistmaker.helpers.PlaceHolder.TRACKS_HISTORY
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.search.ui.TrackSearchViewModel
import com.example.playlistmaker.ui.components.AppBarComponent
import com.example.playlistmaker.uicomponents.ButtonComponent
import com.example.playlistmaker.uicomponents.ErrorView
import com.example.playlistmaker.uicomponents.LoadingComponent
import com.example.playlistmaker.uicomponents.TrackItemComponent

@Composable
fun SearchScreen(
    viewModel: TrackSearchViewModel,
    clickOnTrack: (TrackDto) -> Unit,
    clearHistory: () -> Unit,
    clearSearchForm: () -> Unit
) {
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
                },
                clearSearchForm = clearSearchForm
            )

            when (state) {
                is SearchScreenState.Loading -> LoadingComponent()
                is SearchScreenState.Success -> SearchContent(
                    trackList = (state as SearchScreenState.Success).tracks,
                    query = query,
                    onSearchClicked = clickOnTrack,
                    placeholder = SEARCH_RESULT
                )

                is SearchScreenState.ShowHistory -> SearchContent(
                    trackList = (state as SearchScreenState.ShowHistory).tracks,
                    query = query,
                    onSearchClicked = clickOnTrack,
                    placeholder = TRACKS_HISTORY,
                    onClearHistory = clearHistory
                )

                is SearchScreenState.NothingFound -> NothingFoundComponent()
                is SearchScreenState.Error -> NoConnectionComponent()
            }

        }
    }

}

@Composable
private fun NothingFoundComponent() {
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

@Composable
private fun NoConnectionComponent() {
    ErrorView(
        icon = painterResource(R.drawable.no_internet),
        text = stringResource(R.string.connection_problem),
        showRetry = false,
        onRetry = {},
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun SearchContent(
    trackList: List<TrackDto>,
    query: String,
    onSearchClicked: (TrackDto) -> Unit,
    placeholder: PlaceHolder,
    onClearHistory: () -> Unit = { }
) {
    when (placeholder) {
        SEARCH_RESULT -> LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            items(trackList) { track ->
                TrackItemComponent(track = track, onClick = { onSearchClicked(track) })
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
                        TrackItemComponent(track = track, onClick = { onSearchClicked(track) })
                    }
                    item {
                        ButtonComponent(onClickAction = onClearHistory)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    clearSearchForm: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp, top = 8.dp, bottom = 8.dp)
                .height(36.dp),
            textStyle = LocalTextStyle.current.copy(
                color = colorResource(R.color.black),
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontSize = 14.sp
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = query,
                    innerTextField = {
                        innerTextField()
                    },
                    shape = RoundedCornerShape(8.dp),
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
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
                    trailingIcon = {
                        if (!query.isEmpty()) {
                            IconButton(onClick = clearSearchForm) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clear_search),
                                    contentDescription = null,
                                    tint = colorResource(R.color.grey)
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorResource(R.color.edit_text_color),
                        unfocusedTextColor = colorResource(R.color.edit_text_color),
                        focusedTextColor = colorResource(R.color.edit_text_color),
                        focusedIndicatorColor = colorResource(R.color.edit_text_color),
                        unfocusedContainerColor = colorResource(R.color.edit_text_color),
                        unfocusedIndicatorColor = colorResource(R.color.edit_text_color)
                    ),
                    contentPadding = PaddingValues(0.dp),
                )

            }
        )
    }
}