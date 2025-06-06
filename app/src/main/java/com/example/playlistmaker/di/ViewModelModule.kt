package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.viewModels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.TrackSearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { SettingsViewModel(settingsInteractor = get()) }
    viewModel { TrackSearchViewModel(trackInteractor = get()) }
    viewModel { PlayerViewModel(playerInteractor = get(), trackInteractor = get()) }
    viewModel { FavoritesViewModel(interactor = get()) }
    viewModel { PlaylistsViewModel() }

}