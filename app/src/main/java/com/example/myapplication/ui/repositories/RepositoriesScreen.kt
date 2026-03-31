package com.example.myapplication.ui.repositories

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel

/**
 * Square org repos: one scaffold, list + loading/error/empty.
 * [RepositoriesScreenContent] is public so tests/previews can pass a fixed [RepositoriesUiState] without Koin.
 */
@Composable
fun RepositoriesScreen(
    viewModel: RepositoriesViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    RepositoriesScreenContent(
        state = state,
        onRetry = viewModel::load,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoriesScreenContent(
    state: RepositoriesUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(Modifier.padding(bottom = 4.dp)) {
                        Text(
                            text = stringResource(R.string.repos_screen_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.repos_screen_subtitle),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (state) {
                RepositoriesUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag(TAG_LOADING),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(RepositoriesDimens.LoadingVerticalSpacing),
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = RepositoriesDimens.LoadingIndicatorStroke,
                            modifier = Modifier.size(RepositoriesDimens.LoadingIndicatorSize),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        Text(
                            text = stringResource(R.string.loading_repositories),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                is RepositoriesUiState.Content -> {
                    if (state.repos.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(RepositoriesDimens.EmptyErrorPadding)
                                .testTag(TAG_EMPTY),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(RepositoriesDimens.EmptyVerticalSpacing),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FolderOpen,
                                contentDescription = null,
                                modifier = Modifier.size(RepositoriesDimens.EmptyStateIconSize),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            )
                            Text(
                                text = stringResource(R.string.repos_empty),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = RepositoriesDimens.ListHorizontalPadding,
                                end = RepositoriesDimens.ListHorizontalPadding,
                                top = RepositoriesDimens.ListTopPadding,
                                bottom = RepositoriesDimens.ListBottomPadding,
                            ),
                            verticalArrangement = Arrangement.spacedBy(RepositoriesDimens.CardSpacing),
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag(TAG_LIST),
                        ) {
                            items(
                                items = state.repos,
                                key = { it.fullName.ifBlank { it.name } },
                            ) { repo ->
                                RepositoryCard(
                                    repo = repo,
                                    onClick = {
                                        val path = repo.fullName.trim().trimStart('/')
                                        val url = "https://github.com/$path"
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse(url)),
                                        )
                                    },
                                )
                            }
                        }
                    }
                }

                is RepositoriesUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(RepositoriesDimens.EmptyErrorPadding)
                            .testTag(TAG_ERROR),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(RepositoriesDimens.LoadingVerticalSpacing),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(RepositoriesDimens.ErrorStateIconSize),
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        if (state.canRetry) {
                            Button(onClick = onRetry) {
                                Text(text = stringResource(R.string.action_retry))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenContentLoadingPreview() {
    MyApplicationTheme {
        RepositoriesScreenContent(
            state = RepositoriesUiState.Loading,
            onRetry = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenContentErrorPreview() {
    MyApplicationTheme {
        RepositoriesScreenContent(
            state = RepositoriesUiState.Error("Network error"),
            onRetry = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenContentListPreview() {
    MyApplicationTheme {
        RepositoriesScreenContent(
            state = RepositoriesUiState.Content(
                listOf(
                    RepoUiModel(
                        name = "okhttp",
                        fullName = "square/okhttp",
                        description = "Square’s meticulous HTTP client for Java and Kotlin.",
                        language = "Kotlin",
                        licenseSpdxId = "Apache-2.0",
                        stargazersCount = 48_000,
                        forksCount = 9_200,
                        updatedAtDisplay = "30 Mar, 2026",
                        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4",
                    ),
                    RepoUiModel(
                        name = "retrofit",
                        fullName = "square/retrofit",
                        description = "",
                        language = "",
                        licenseSpdxId = "MIT",
                        stargazersCount = 12_000,
                        forksCount = 2_400,
                        updatedAtDisplay = "12 Jan, 2025",
                        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4",
                    ),
                ),
            ),
            onRetry = {},
        )
    }
}
