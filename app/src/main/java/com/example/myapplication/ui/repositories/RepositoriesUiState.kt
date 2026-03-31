package com.example.myapplication.ui.repositories

/** Everything the list row needs — plain strings/ints for Compose. */
data class RepoUiModel(
    val name: String,
    val fullName: String,
    val description: String,
    val language: String?,
    val licenseSpdxId: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val updatedAtDisplay: String?,
    val ownerAvatarUrl: String?,
)

/** Single screen, three outcomes: spinner, list (maybe empty), or error + optional retry. */
sealed interface RepositoriesUiState {
    data object Loading : RepositoriesUiState
    data class Content(val repos: List<RepoUiModel>) : RepositoriesUiState
    data class Error(val message: String, val canRetry: Boolean = true) : RepositoriesUiState
}
