package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.viewModels.BottomSheetViewModel
import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewModels.NewPlayListViewModel
import com.example.playlistmaker.library.ui.viewModels.PlaylistMenuBottomSheetViewModel
import com.example.playlistmaker.library.ui.viewModels.PlaylistViewModel
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.TrackSearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SettingsViewModel).bind()
    viewModelOf(::TrackSearchViewModel).bind()
    viewModelOf(::PlayerViewModel).bind()
    viewModelOf(::FavoritesViewModel).bind()
    viewModelOf(::PlaylistsViewModel).bind()
    viewModelOf(::NewPlayListViewModel).bind()
    viewModelOf(::BottomSheetViewModel).bind()
    viewModelOf(::PlaylistViewModel).bind()
    viewModelOf(::PlaylistMenuBottomSheetViewModel).bind()
}