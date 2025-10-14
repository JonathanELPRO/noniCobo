package com.calyrsoft.ucbp1.features.movie.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    vm: MoviesViewModel = koinViewModel(),
    onClick: (MovieModel) -> Unit
) {
    val state by vm.state.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val s = state) {
            is MoviesViewModel.UiState.Init -> {
                Text("Init", modifier = Modifier.align(Alignment.Center))
            }

            is MoviesViewModel.UiState.Loading -> {
                Text("Cargando...", modifier = Modifier.align(Alignment.Center))
            }

            is MoviesViewModel.UiState.Error -> {
                Text("Error: ${s.message}", modifier = Modifier.align(Alignment.Center))
            }

            is MoviesViewModel.UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.movies) { movie ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClick(movie) }
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                AsyncImage(
                                    model = movie.imageUrl,
                                    contentDescription = movie.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = movie.title.toString(),
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )

                                    IconButton(
                                        onClick = { vm.addToFavorites(movie) }
                                    ) {
                                        Icon(
                                            imageVector = if (movie.isFavorite) {
                                                Icons.Filled.Favorite
                                            } else {
                                                Icons.Filled.FavoriteBorder
                                            },
                                            contentDescription = if (movie.isFavorite) {
                                                "Quitar de favoritos"
                                            } else {
                                                "Agregar a favoritos"
                                            },
                                            tint = if (movie.isFavorite) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurface
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
