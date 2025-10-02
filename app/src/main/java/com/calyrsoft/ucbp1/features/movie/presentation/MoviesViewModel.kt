package com.calyrsoft.ucbp1.features.movie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetFavoritesUseCase
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.features.movie.domain.usecase.InserteMyFavoriteMovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val insertFavorite: InserteMyFavoriteMovieUseCase,
    private val getLocalFavorites: GetFavoritesUseCase

) : ViewModel() {

    sealed class UiState {
        object Init : UiState()
        object Loading : UiState()
        data class Success(val movies: List<MovieModel>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Init)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        load(1)
    }

    fun load(page: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState.Loading
            val result = getPopularMovies(page)
            result.fold(
                onSuccess = { movies ->

                    val localFavorites = getLocalFavorites()
                    val favoriteIds = localFavorites.map { it.id }.toSet()

                    val updatedMovies = movies.map { movie ->
                        if (favoriteIds.contains(movie.id)) {
                            movie.copy(isFavorite = true)
                        } else {
                            movie
                        }
                    }

                    val sorted = updatedMovies.sortedByDescending { it.isFavorite }

                    _state.value = UiState.Success(sorted)
                },
                onFailure = { exception ->
                    _state.value = UiState.Error(exception.message ?: "Error desconocido")
                }
            )
        }
    }



    fun addToFavorites(movie: MovieModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _state.value
            if (currentState is UiState.Success) {

                val favoriteMovie = movie.copy(isFavorite = true)
                insertFavorite(favoriteMovie)

                val updatedList = currentState.movies.map {
                    if (it.id == movie.id) favoriteMovie else it
                }.sortedByDescending { it.isFavorite }

                _state.value = UiState.Success(updatedList)
            }
        }
    }

}
