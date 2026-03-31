package com.example.myapplication.data.remote.dto

import com.squareup.moshi.Json

data class LicenseDto(
    @param:Json(name = "spdx_id")
    val spdxId: String?,
)
