package com.blackfoxis.tmdb.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.blackfoxis.tmdb.Constants
import com.blackfoxis.tmdb.R
import com.blackfoxis.tmdb.model.MovieDetails
import com.blackfoxis.tmdb.model.MovieDetailsViewModel
import com.blackfoxis.tmdb.model.ProductionCompany

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieDetails = viewModel.movieDetails
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        movieDetails?.title ?: stringResource(id = R.string.movie_details_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_navigate_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                isLoading -> FullScreenLoading()
                error != null -> ErrorDisplay(error = error, onRetry = { viewModel.retryLoadMovieDetails() })
                movieDetails != null -> MovieDetailsContent(movie = movieDetails)
                else -> NoDataDisplay()
            }
        }
    }
}


@Composable
fun MovieDetailsContent(movie: MovieDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Constants.IMAGE_BASE_URL + Constants.BACKDROP_SIZE_LARGE + movie.backdropPath)
                    .crossfade(true)
                    .build(),
                placeholder = rememberVectorPainter(Icons.Filled.Place),
                error = rememberVectorPainter(Icons.Filled.Star),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 300f
                        )
                    )
            )

            movie.posterPath?.let { poster ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                        .width(130.dp)
                        .aspectRatio(2f / 3f)
                ) {
                    AsyncImage(
                        model = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_LARGE + poster,
                        contentDescription = movie.title + " poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // --- Основная информация ---
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = movie.title ?: stringResource(id = R.string.unknown_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            movie.tagline?.takeIf { it.isNotBlank() }?.let { tagline ->
                Text(
                    text = "\"$tagline\"",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Рейтинг
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = stringResource(id = R.string.cd_rating_icon),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(
                        R.string.movie_rating_format,
                        movie.voteAverage ?: 0.0,
                        movie.voteCount ?: 0
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Жанры
            movie.genres?.takeIf { it.isNotEmpty() }?.let { genres ->
                SectionTitle(title = stringResource(R.string.section_title_genres))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(genres) { genre ->
                        genre.name?.let { GenreChip(text = it) }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Обзор
            SectionTitle(title = stringResource(R.string.section_title_overview))
            Text(
                text = movie.overview ?: stringResource(id = R.string.overview_not_available),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Дополнительные детали ---
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            DetailsRow(label = stringResource(R.string.detail_label_release_date), value = movie.releaseDate)
            movie.runtime?.let {
                DetailsRow(label = stringResource(R.string.detail_label_runtime), value = stringResource(R.string.runtime_format_minutes, it))
            }
            DetailsRow(label = stringResource(R.string.detail_label_status), value = movie.status)
            movie.originalTitle?.takeIf { it != movie.title }?.let {
                DetailsRow(label = stringResource(R.string.detail_label_original_title), value = it)
            }

            // Языки
            movie.spokenLanguages?.takeIf { it.isNotEmpty() }?.let { languages ->
                DetailsRow(
                    label = stringResource(R.string.detail_label_languages),
                    value = languages.mapNotNull { it.englishName ?: it.name }.joinToString(", ")
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Компании производители
            movie.productionCompanies?.takeIf { it.isNotEmpty() }?.let { companies ->
                SectionTitle(title = stringResource(R.string.section_title_production_companies))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(companies) { company ->
                        ProductionCompanyItem(company)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Страны производители
            movie.productionCountries?.takeIf { it.isNotEmpty() }?.let { countries ->
                SectionTitle(title = stringResource(R.string.section_title_production_countries))
                Text(
                    text = countries.mapNotNull { it.name }.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun GenreChip(text: String) {
    SuggestionChip(
        onClick = {  },
        label = { Text(text, style = MaterialTheme.typography.labelSmall) },
        shape = CircleShape
    )
}

@Composable
fun DetailsRow(label: String, value: String?) {
    value?.takeIf { it.isNotBlank() }?.let {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.6f)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun ProductionCompanyItem(company: ProductionCompany) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        company.logoPath?.let { logoPath ->
            AsyncImage(
                model = Constants.IMAGE_BASE_URL + Constants.LOGO_SIZE_SMALL + logoPath,
                contentDescription = company.name + " logo",
                modifier = Modifier
                    .size(60.dp)
                    .padding(bottom = 4.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit,
                error = rememberVectorPainter(Icons.Filled.Star)
            )
        } ?: Box(
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 4.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        company.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorDisplay(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_occurred_format, error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.button_retry))
        }
    }
}

@Composable
fun NoDataDisplay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_movie_details_loaded),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}