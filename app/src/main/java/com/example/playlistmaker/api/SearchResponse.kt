package com.example.playlistmaker.api

import com.example.playlistmaker.model.Track

class SearchResponse (val resultCount: Int, val results: List<Track>)