package com.blackfoxis.tmdb.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.blackfoxis.tmdb.network.MovieApi
import com.blackfoxis.tmdb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    var movies by mutableStateOf<List<Movie>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            isLoading = true
            error = null // Сбрасываем предыдущую ошибку
            try {
                movies = movieRepository.getPopularMovies()
            } catch (e: Exception) {
                Log.e("MainScreenVM", "Error fetching popular movies", e)
                error = e.localizedMessage ?: "Ошибка загрузки популярных фильмов"
            }
            isLoading = false
        }
    }
}