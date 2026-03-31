package com.example.myapplication.data.remote.dto

import com.squareup.moshi.Json

data class OwnerDto(
    @param:Json(name = "avatar_url")
    val avatarUrl: String?,
)
