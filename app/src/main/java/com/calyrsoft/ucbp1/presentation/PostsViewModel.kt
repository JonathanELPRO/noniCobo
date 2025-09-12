package com.calyrsoft.ucbp1.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.domain.model.CommentModel
import com.calyrsoft.ucbp1.domain.model.MovieModel
import com.calyrsoft.ucbp1.domain.model.PostModel
import com.calyrsoft.ucbp1.domain.usecase.GetCommentsForOnePostUseCase
import com.calyrsoft.ucbp1.domain.usecase.GetPopularMoviesUseCase
import com.calyrsoft.ucbp1.domain.usecase.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val getCommentsForOnePostUseCase: GetCommentsForOnePostUseCase
) : ViewModel() {

    data class SuccessWithComments(
        val posts: List<PostModel>,
        val commentsByPost: Map<String, List<CommentModel>> = emptyMap(),
        val expandedPostId: String? = null
    )

    sealed class UiState {
        object Init : UiState()
        object Loading : UiState()
        data class Success(val data: SuccessWithComments) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Init)
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = UiState.Loading
            val result = getPostsUseCase()
            result.fold(
                onSuccess = { posts ->
                    _state.value = UiState.Success(SuccessWithComments(posts))
                },
                onFailure = { _state.value = UiState.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = _state.value
            val current = (_state.value as? UiState.Success)?.data ?: return@launch
            val result = getCommentsForOnePostUseCase(postId)
            result.fold(
                onSuccess = { comments ->
                    _state.value = UiState.Success(
                        current.copy(
                            commentsByPost = current.commentsByPost + (postId to comments),
                            expandedPostId = if (current.expandedPostId == postId) null else postId
                        )
                    //Este método de .copy  crea un nuevo objeto con los mismos valores de current, pero te permite sobreescribir solo los campos que quieras.
                    )
                },
                onFailure = { ex ->
                    _state.value = UiState.Error(ex.message ?: "Error al cargar comentarios")
                }
            )
        }
    }
}
