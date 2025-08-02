package com.blackfoxis.tmdb.network

import com.blackfoxis.tmdb.Constants
import com.blackfoxis.tmdb.model.MovieDetails
import com.blackfoxis.tmdb.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String,
    ): MovieResponse
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int, // Значение для {movie_id} в URL
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String
    ): MovieDetails
}