package com.example.moviedb.models

data class Movie(
    var id: Long = 0L,
    var title: String,
    var posterPath: String,
    var backdropPath: String,
    var releaseDate: String,
    var overview: String,
    val genres: List<Map<String, Any>>,
    val homepage: String = "",
    val imdbId: String = ""
)
