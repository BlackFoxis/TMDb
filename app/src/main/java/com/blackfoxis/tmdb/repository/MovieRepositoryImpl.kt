package com.blackfoxis.tmdb.repository

import android.util.Log
import com.blackfoxis.tmdb.Constants
import com.blackfoxis.tmdb.di.LanguageProvider
import com.blackfoxis.tmdb.model.Movie
import com.blackfoxis.tmdb.model.MovieDetails
import com.blackfoxis.tmdb.network.MovieApiService
import javax.inject.Inject
import javax.inject.Named


class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    @Named("TMDB_API_KEY") private val apiKey: String, // <--- ВНЕДРЯЕМ API ключ
    private val languageProvider: LanguageProvider    // <--- ВНЕДРЯЕМ LanguageProvider
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val currentLanguage = languageProvider.getTmdbLanguageCode()
        return try {
            val response = movieApiService.getPopularMovies(
                apiKey = apiKey,
                language = currentLanguage

            )
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails? {
        val currentLanguage = languageProvider.getTmdbLanguageCode()
        Log.d("MovieRepoImpl", "Fetching movie details (ID $movieId) with lang: $currentLanguage")
        return try {
            movieApiService.getMovieDetails(
                movieId = movieId,
                apiKey = apiKey,
                language = currentLanguage
            )
        } catch (e: Exception) {
            Log.e("MovieRepoImpl", "Error getMovieDetails for ID $movieId: ${e.message}", e)
            null
        }
    }
}