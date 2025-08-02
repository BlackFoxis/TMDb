package com.blackfoxis.tmdb.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.blackfoxis.tmdb.model.MainScreenViewModel
import com.blackfoxis.tmdb.model.Movie
import com.blackfoxis.tmdb.ui.components.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val movies = viewModel.movies
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Популярные фильмы") })
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                // Загрузки
                Text("Загрузка...", modifier = Modifier.padding(paddingValues))
            }
            error != null -> {
                // Сообщение об ошибке
                Text("Ошибка: $error", modifier = Modifier.padding(paddingValues))
            }
            else -> {
                PopularMoviesList(
                    movies = movies,
                    onMovieClick = onMovieClick,
                    paddingValues = paddingValues
                )
            }
        }
    }
}
@Composable
fun PopularMoviesList(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(movies, key = { movie -> movie.id }) { movie ->
            MovieItem(movie = movie, onMovieClick = { onMovieClick(movie.id) })
        }
    }
}

