package com.blackfoxis.tmdb.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.blackfoxis.tmdb.navigation.NavRoutes
import com.blackfoxis.tmdb.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var movieDetails by mutableStateOf<MovieDetails?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private val movieId: Int? = savedStateHandle[NavRoutes.MOVIE_ID_ARG] // Более краткий доступ

    init {
        if (movieId != null && movieId != 0) {
            loadMovieDetails(movieId)
        } else {
            val errorMessage = "Неверный ID фильма: $movieId"
            Log.e("MovieDetailsVM", errorMessage)
            error = errorMessage
        }
    }

    private fun loadMovieDetails(idToLoad: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val details = movieRepository.getMovieDetails(idToLoad)
                movieDetails = details
                if (details == null) {
                    Log.w("MovieDetailsVM", "Детали для ID $idToLoad не найдены (null от репозитория)")
                }
            } catch (e: IOException) {
                Log.e("MovieDetailsVM", "Сетевая ошибка при загрузке деталей для ID $idToLoad", e)
                error = "Ошибка сети. Проверьте подключение."
            } catch (e: Exception) {
                Log.e("MovieDetailsVM", "Ошибка при загрузке деталей для ID $idToLoad", e)
                error = "Не удалось загрузить детали фильма."
            }
            isLoading = false
        }
    }

    fun retryLoadMovieDetails() {
        if (movieId != null && movieId != 0) {
            Log.d("MovieDetailsVM", "Повторная загрузка для movieId: $movieId")
            loadMovieDetails(movieId)
        } else {
            Log.e("MovieDetailsVM", "Невозможно повторить: неверный ID фильма: $movieId")
        }
    }
}