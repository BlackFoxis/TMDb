package com.blackfoxis.tmdb.repository

import com.blackfoxis.tmdb.model.Movie
import com.blackfoxis.tmdb.model.MovieDetails

interface MovieRepository {

    suspend fun getPopularMovies(): List<Movie> // Метод для получения популярных фильмов
    suspend fun getMovieDetails(movieId: Int): MovieDetails? // Метод для получения деталей фильма
}