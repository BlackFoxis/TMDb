package com.blackfoxis.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.blackfoxis.tmdb.navigation.AppNavigation
import com.blackfoxis.tmdb.ui.theme.TMDbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TMDbTheme {
                AppNavigation()
            }
        }
    }
}




