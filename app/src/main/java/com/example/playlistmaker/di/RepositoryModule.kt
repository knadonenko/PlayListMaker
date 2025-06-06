package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.LibraryRepository
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.SettingsRepoImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> { TrackRepositoryImpl(networkClient = get(), localStorage = get()) }
    single<SettingsRepository> { SettingsRepoImpl(themeStorage = get()) }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(playerClient = get())
    }

    singleOf(::FavoritesRepositoryImpl).bind<LibraryRepository>()

}