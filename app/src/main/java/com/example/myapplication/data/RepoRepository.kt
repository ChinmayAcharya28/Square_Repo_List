package com.example.myapplication.data

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.remote.GitHubApi
import com.example.myapplication.domain.model.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Small abstraction so tests can fake repos without Retrofit. */
fun interface RepoRepository {
    suspend fun getSquareRepos(): Result<List<Repo>>
}

class DefaultRepoRepository(
    private val api: GitHubApi,
) : RepoRepository {

    // IO: network call; map JSON DTOs to domain before they reach the UI layer.
    override suspend fun getSquareRepos(): Result<List<Repo>> = withContext(Dispatchers.IO) {
        runCatching {
            api.listOrgRepos(org = GITHUB_ORG_SQUARE, page = 1, perPage = 100).map { it.toDomain() }
        }
    }

    private companion object {
        const val GITHUB_ORG_SQUARE = "square"
    }
}
