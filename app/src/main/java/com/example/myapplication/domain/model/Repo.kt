package com.example.myapplication.domain.model

/** App-facing repo fields after DTO mapping; stays independent of Retrofit/Moshi. */
data class Repo(
    val name: String,
    /** GitHub `owner/repo` path; used for repo URLs. */
    val fullName: String = "",
    val description: String?,
    val language: String? = null,
    val licenseSpdxId: String? = null,
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val updatedAt: String? = null,
    val ownerAvatarUrl: String? = null,
)
