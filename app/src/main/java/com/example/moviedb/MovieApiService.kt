package com.example.moviedb

import com.example.moviedb.models.Movie
import com.example.moviedb.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.moviedb.models.ReviewResponse

interface MovieApiService {
    // get list of movies for grid
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    // get genre, imdb
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String
    ): Movie

    // get review
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String
    ): ReviewResponse
}