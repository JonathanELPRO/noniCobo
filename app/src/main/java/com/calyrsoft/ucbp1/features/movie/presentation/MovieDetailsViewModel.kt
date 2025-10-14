package com.calyrsoft.ucbp1.features.movie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.movie.domain.model.MovieModel
import com.calyrsoft.ucbp1.features.movie.domain.usecase.GetMovieByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieModel?>(null)
    val movie: StateFlow<MovieModel?> = _movie

    fun loadMovie(id: Long) {
        viewModelScope.launch {
            _movie.value = getMovieByIdUseCase(id)
        }
    }
}

