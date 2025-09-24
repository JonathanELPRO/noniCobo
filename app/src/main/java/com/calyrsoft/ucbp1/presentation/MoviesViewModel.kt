package com.calyrsoft.ucbp1.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.MovieModel
import com.calyrsoft.ucbp1.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase
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
                onSuccess = { movies -> _state.value = UiState.Success(movies) },
                onFailure = { exception -> _state.value = UiState.Error(exception.message ?: "Error desconocido") }
            )
        }
    }
}
