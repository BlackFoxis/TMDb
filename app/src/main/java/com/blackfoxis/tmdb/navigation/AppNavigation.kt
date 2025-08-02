package com.blackfoxis.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController // <-- ПРАВИЛЬНЫЙ ИМПОРТ
import androidx.navigation.navArgument
import com.blackfoxis.tmdb.network.MovieApi
import com.blackfoxis.tmdb.repository.MovieRepository
import com.blackfoxis.tmdb.repository.MovieRepositoryImpl
import com.blackfoxis.tmdb.ui.screens.MainScreen
import com.blackfoxis.tmdb.ui.screens.MovieDetailsScreen


object NavRoutes {
    const val MAIN_SCREEN = "mainScreen"
    const val MOVIE_DETAILS_SCREEN_ROUTE = "movieDetails"
    const val MOVIE_ID_ARG = "movieId"
    const val MOVIE_DETAILS_FULL_ROUTE = "$MOVIE_DETAILS_SCREEN_ROUTE/{$MOVIE_ID_ARG}"

    fun movieDetails(movieId: Int) = "$MOVIE_DETAILS_SCREEN_ROUTE/$movieId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.MAIN_SCREEN) {
        composable(NavRoutes.MAIN_SCREEN) {
            MainScreen(
                onMovieClick = { movieId ->
                    navController.navigate(NavRoutes.movieDetails(movieId))
                }
            )
        }
        composable(
            route = NavRoutes.MOVIE_DETAILS_FULL_ROUTE,
            arguments = listOf(navArgument(NavRoutes.MOVIE_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(NavRoutes.MOVIE_ID_ARG)
            if (movieId != null) {
                MovieDetailsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}