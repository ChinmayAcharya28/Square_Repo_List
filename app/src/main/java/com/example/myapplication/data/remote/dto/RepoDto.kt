package com.example.myapplication.data.remote.dto

import com.squareup.moshi.Json

// Mirrors GitHub org repo JSON; snake_case fields need @Json names.
data class RepoDto(
    val name: String,
    @param:Json(name = "full_name")
    val fullName: String? = null,
    val description: String?,
    val language: String? = null,
    val license: LicenseDto? = null,
    @param:Json(name = "stargazers_count")
    val stargazersCount: Int = 0,
    @param:Json(name = "forks_count")
    val forksCount: Int = 0,
    @param:Json(name = "updated_at")
    val updatedAt: String? = null,
    val owner: OwnerDto,
)
