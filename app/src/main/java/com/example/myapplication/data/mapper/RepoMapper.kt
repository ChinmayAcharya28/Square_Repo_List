package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.RepoDto
import com.example.myapplication.domain.model.Repo

// Flattens nested JSON (license, owner) into a single domain type.
fun RepoDto.toDomain(): Repo = Repo(
    name = name,
    fullName = fullName?.takeIf { it.isNotBlank() } ?: "square/$name",
    description = description,
    language = language,
    licenseSpdxId = license?.spdxId?.takeIf { it.isNotBlank() },
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    updatedAt = updatedAt,
    ownerAvatarUrl = owner.avatarUrl,
)
