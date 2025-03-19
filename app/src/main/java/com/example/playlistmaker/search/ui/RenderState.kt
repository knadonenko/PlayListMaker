package com.example.playlistmaker.search.ui

interface RenderState {
    fun render(state: SearchState)
    fun showSearch()
    fun showHistory()
    fun showNotFound()
    fun showNetworkError()
    fun showProgressBar()
}