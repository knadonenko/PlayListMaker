package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.example.playlistmaker.db.TrackDataBase
import com.example.playlistmaker.helpers.SharedPrefsConstants
import com.example.playlistmaker.player.data.PlayListMediaPlayer
import com.example.playlistmaker.player.data.PlayerClient
import com.example.playlistmaker.search.data.LocalStorage
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.network.NetworkClient
import com.example.playlistmaker.search.network.RetrofitNetworkClient
import com.example.playlistmaker.search.network.SearchAPI
import com.example.playlistmaker.settings.data.ThemeStorage
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    factory { Gson() }

    single { Handler(Looper.getMainLooper()) }

    single {
        androidContext().getSharedPreferences(
            SharedPrefsConstants.PLAYLIST_MAKER_PREFS.prefName,
            Context.MODE_PRIVATE
        )
    }

    single {
        Room.databaseBuilder(androidContext(), TrackDataBase::class.java, "database.db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { ThemeStorage(sp = get()) }

    single<NetworkClient> { RetrofitNetworkClient(androidContext()) }

    single<SearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchAPI::class.java)
    }

    single<LocalStorage> {
        TrackStorage(
            preferences = get()
        )
    }

    factory<PlayerClient> {
        PlayListMediaPlayer(mediaPlayer = get())
    }

    factory {
        MediaPlayer()
    }

}