package com.blackfoxis.tmdb.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.blackfoxis.tmdb.Constants
import com.blackfoxis.tmdb.model.Movie

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMovieClick(movie.id) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            movie.poster_path?.let {
                Image(
                    painter = rememberAsyncImagePainter(Constants.IMAGE_BASE_URL + it),
                    contentDescription = movie.title,
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                movie.overview?.let { overview ->
                    Text(overview.take(100) + "...", maxLines = 3)
                } ?: Text("Описание отсутствует", maxLines = 3)
            }
        }
    }
}