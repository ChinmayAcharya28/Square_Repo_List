package com.example.myapplication.ui.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Loads Square org repos once on start; [load] retries after errors.
 * Formatting and UI fields are applied in `RepoUiModelMapper`, not here.
 */
class RepositoriesViewModel(
    private val repository: RepoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RepositoriesUiState>(RepositoriesUiState.Loading)
    val uiState: StateFlow<RepositoriesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = RepositoriesUiState.Loading
            repository.getSquareRepos()
                .fold(
                    onSuccess = { repos ->
                        _uiState.value = RepositoriesUiState.Content(
                            repos.map { it.toUiModel() },
                        )
                    },
                    onFailure = { throwable ->
                        _uiState.value = RepositoriesUiState.Error(
                            // GitHub errors handling; fall back if empty.
                            message = throwable.message.orEmpty().ifBlank { "Something went wrong" },
                        )
                    },
                )
        }
    }
}
