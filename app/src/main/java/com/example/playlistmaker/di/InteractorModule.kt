package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.LibraryInteractor
import com.example.playlistmaker.library.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val interactorModule = module {

    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
    singleOf(::TrackInteractorImpl).bind<TrackInteractor>()
    singleOf(::SettingsInteractorImpl).bind<SettingsInteractor>()
    singleOf(::FavoritesInteractorImpl).bind<LibraryInteractor>()

}