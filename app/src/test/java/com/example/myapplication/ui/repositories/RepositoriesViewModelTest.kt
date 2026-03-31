package com.example.myapplication.ui.repositories

import com.example.myapplication.data.RepoRepository
import com.example.myapplication.domain.model.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoriesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun load_success_emitsContent() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "a", description = "d")),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val state = viewModel.uiState.value
        assertTrue(state is RepositoriesUiState.Content)
        val content = state as RepositoriesUiState.Content
        assertEquals(1, content.repos.size)
        assertEquals("a", content.repos[0].name)
        assertEquals("d", content.repos[0].description)
        assertNull(content.repos[0].language)
        assertNull(content.repos[0].ownerAvatarUrl)
        assertNull(content.repos[0].licenseSpdxId)
        assertEquals(0, content.repos[0].stargazersCount)
        assertEquals(0, content.repos[0].forksCount)
        assertNull(content.repos[0].updatedAtDisplay)
        assertEquals("square/a", content.repos[0].fullName)
    }

    @Test
    fun load_success_passesLanguageThrough() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "a", description = "d", language = "Ruby")),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals("Ruby", content.repos[0].language)
    }

    @Test
    fun load_success_passesLicenseSpdxIdThrough() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "a", description = "d", licenseSpdxId = "MIT")),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals("MIT", content.repos[0].licenseSpdxId)
    }

    @Test
    fun load_success_passesStargazersCountThrough() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "a", description = "d", stargazersCount = 99)),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals(99, content.repos[0].stargazersCount)
    }

    @Test
    fun load_success_passesForksCountThrough() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "a", description = "d", forksCount = 500)),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals(500, content.repos[0].forksCount)
    }

    @Test
    fun load_success_formatsUpdatedAtForUi() = runTest {
        val previousTz = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            val repo = object : RepoRepository {
                override suspend fun getSquareRepos() = Result.success(
                    listOf(
                        Repo(
                            name = "a",
                            description = "d",
                            updatedAt = "2011-01-26T19:06:43Z",
                        ),
                    ),
                )
            }
            val viewModel = RepositoriesViewModel(repo)
            val content = viewModel.uiState.value as RepositoriesUiState.Content
            assertEquals("26 Jan, 2011", content.repos[0].updatedAtDisplay)
        } finally {
            TimeZone.setDefault(previousTz)
        }
    }

    @Test
    fun load_success_passesOwnerAvatarUrlThrough() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(
                    Repo(
                        name = "a",
                        description = "d",
                        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                    ),
                ),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals(
            "https://avatars.githubusercontent.com/u/1?v=4",
            content.repos[0].ownerAvatarUrl,
        )
    }

    @Test
    fun load_success_mapsBlankDescriptionToEmptyStringForUi() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.success(
                listOf(Repo(name = "x", description = null)),
            )
        }
        val viewModel = RepositoriesViewModel(repo)
        val content = viewModel.uiState.value as RepositoriesUiState.Content
        assertEquals("", content.repos[0].description)
    }

    @Test
    fun load_failure_emitsError() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.failure<List<Repo>>(RuntimeException("boom"))
        }
        val viewModel = RepositoriesViewModel(repo)
        val state = viewModel.uiState.value
        assertTrue(state is RepositoriesUiState.Error)
        assertEquals("boom", (state as RepositoriesUiState.Error).message)
    }

    @Test
    fun load_failure_usesFallbackMessageWhenThrowableMessageBlank() = runTest {
        val repo = object : RepoRepository {
            override suspend fun getSquareRepos() = Result.failure<List<Repo>>(RuntimeException(""))
        }
        val viewModel = RepositoriesViewModel(repo)
        val state = viewModel.uiState.value as RepositoriesUiState.Error
        assertEquals("Something went wrong", state.message)
    }
}
