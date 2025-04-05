package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

//    lateinit var themeSwitcherInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        startKoin {
//            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    interactorModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.isDarkModeOn()
    }

}