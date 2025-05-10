package com.example.playlistmaker.search.network

import com.example.playlistmaker.search.data.Response


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}