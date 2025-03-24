package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchSongs(expression: String, consumer: SearchConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)){
                is Resource.Success ->{consumer.consume(resource.data, null, resource.code)}
                is Resource.Error -> {consumer.consume(null, resource.message, resource.code)}
            }
        }
    }

    override fun addTrackToHistory(track: TrackDto) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(): ArrayList<TrackDto> {
        return repository.getHistory()
    }
}