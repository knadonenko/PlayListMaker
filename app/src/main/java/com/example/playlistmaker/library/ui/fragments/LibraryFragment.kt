package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.components.AppBarComponent
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LibraryScreen()
            }
        }
    }

    @Composable
    @Preview
    fun LibraryScreen() {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

        val tabs = listOf(
            stringResource(R.string.favorite_tracks),
            stringResource(R.string.playlists)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBarComponent(stringResource(R.string.library)) },
            containerColor = colorResource(R.color.settings_main_color)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colorResource(R.color.snackbar),
                    contentColor = colorResource(R.color.settings_text_color),
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.fillMaxWidth().tabIndicatorOffset(pagerState.currentPage),
                            width = Dp.Unspecified,
                            color = colorResource(R.color.blue)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = {
                                Text(
                                    text = title,
                                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                                    fontSize = 16.sp
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> FragmentsContainer(fragment = { FavoritesFragment() })
                        1 -> FragmentsContainer(fragment = { PlaylistsFragment() })
                    }
                }
            }
        }
    }

    @Composable
    fun FragmentsContainer(fragment: () -> Fragment) {
        val fragmentContainerId = remember { View.generateViewId() }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                FragmentContainerView(context).apply {
                    id = fragmentContainerId
                    (context as AppCompatActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(id, fragment())
                        .commit()
                }
            }
        )
    }

}