package com.example.playlistmaker.api

import com.example.playlistmaker.data.dto.Track

class SearchResponse (val resultCount: Int, val results: ArrayList<Track>)