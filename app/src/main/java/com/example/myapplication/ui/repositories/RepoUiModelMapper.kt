package com.example.myapplication.ui.repositories

import com.example.myapplication.domain.model.Repo
import com.example.myapplication.util.RepoUpdatedAtFormatter

// Keeps ViewModel free of formatting rules (dates, blank descriptions as "").
internal fun Repo.toUiModel(): RepoUiModel =
    RepoUiModel(
        name = name,
        fullName = fullName.ifBlank { "square/$name" },
        description = description.orEmpty(),
        language = language,
        licenseSpdxId = licenseSpdxId,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        updatedAtDisplay = RepoUpdatedAtFormatter.format(updatedAt),
        ownerAvatarUrl = ownerAvatarUrl,
    )
