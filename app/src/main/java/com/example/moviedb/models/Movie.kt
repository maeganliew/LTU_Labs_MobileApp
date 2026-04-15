package com.example.moviedb.models
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Long,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String = "",
    val overview: String = "",
    val genres: List<Genre> = emptyList(),
    val homepage: String? = null,
    @SerializedName("imdb_id") val imdbId: String? = null
)

data class Genre(
    val id: Int,
    val name: String
)
