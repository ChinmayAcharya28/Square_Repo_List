package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.RepoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Subset of the GitHub REST API used by this app. */
interface GitHubApi {

    @GET("orgs/{org}/repos")
    suspend fun listOrgRepos(
        @Path("org") org: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
    ): List<RepoDto>
}
