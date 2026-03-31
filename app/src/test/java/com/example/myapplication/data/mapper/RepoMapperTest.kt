package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.LicenseDto
import com.example.myapplication.data.remote.dto.OwnerDto
import com.example.myapplication.data.remote.dto.RepoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RepoMapperTest {

    private val owner = OwnerDto(avatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4")

    @Test
    fun toDomain_mapsNameAndDescription() {
        val dto = RepoDto(name = "okhttp", description = "Square’s HTTP client", owner = owner)
        val domain = dto.toDomain()
        assertEquals("okhttp", domain.name)
        assertEquals("square/okhttp", domain.fullName)
        assertEquals("Square’s HTTP client", domain.description)
    }

    @Test
    fun toDomain_mapsFullNameFromApi() {
        val dto = RepoDto(
            name = "okhttp",
            fullName = "square/okhttp",
            description = null,
            owner = owner,
        )
        assertEquals("square/okhttp", dto.toDomain().fullName)
    }

    @Test
    fun toDomain_preservesNullDescription() {
        val dto = RepoDto(name = "wire", description = null, owner = owner)
        val domain = dto.toDomain()
        assertEquals("wire", domain.name)
        assertNull(domain.description)
    }

    @Test
    fun toDomain_mapsLanguage() {
        val dto = RepoDto(name = "okhttp", description = "HTTP", language = "Kotlin", owner = owner)
        val domain = dto.toDomain()
        assertEquals("Kotlin", domain.language)
    }

    @Test
    fun toDomain_nullLanguageWhenOmitted() {
        val dto = RepoDto(name = "x", description = null, owner = owner)
        assertNull(dto.toDomain().language)
    }

    @Test
    fun toDomain_mapsOwnerAvatarUrl() {
        val dto = RepoDto(
            name = "a",
            description = null,
            owner = OwnerDto(avatarUrl = "https://example.com/a.png"),
        )
        assertEquals("https://example.com/a.png", dto.toDomain().ownerAvatarUrl)
    }

    @Test
    fun toDomain_nullOwnerAvatarUrl() {
        val dto = RepoDto(name = "a", description = null, owner = OwnerDto(avatarUrl = null))
        assertNull(dto.toDomain().ownerAvatarUrl)
    }

    @Test
    fun toDomain_mapsLicenseSpdxId() {
        val dto = RepoDto(
            name = "a",
            description = null,
            license = LicenseDto(spdxId = "MIT"),
            owner = owner,
        )
        assertEquals("MIT", dto.toDomain().licenseSpdxId)
    }

    @Test
    fun toDomain_mapsStargazersCount() {
        val dto = RepoDto(
            name = "a",
            description = null,
            stargazersCount = 42_000,
            owner = owner,
        )
        assertEquals(42_000, dto.toDomain().stargazersCount)
    }

    @Test
    fun toDomain_mapsForksCount() {
        val dto = RepoDto(
            name = "a",
            description = null,
            forksCount = 1_234,
            owner = owner,
        )
        assertEquals(1_234, dto.toDomain().forksCount)
    }

    @Test
    fun toDomain_mapsUpdatedAt() {
        val dto = RepoDto(
            name = "a",
            description = null,
            updatedAt = "2011-01-26T19:06:43Z",
            owner = owner,
        )
        assertEquals("2011-01-26T19:06:43Z", dto.toDomain().updatedAt)
    }

    @Test
    fun toDomain_nullLicenseSpdxIdWhenMissingOrBlank() {
        assertNull(
            RepoDto(name = "a", description = null, license = null, owner = owner)
                .toDomain()
                .licenseSpdxId,
        )
        assertNull(
            RepoDto(
                name = "a",
                description = null,
                license = LicenseDto(spdxId = "   "),
                owner = owner,
            ).toDomain().licenseSpdxId,
        )
    }
}
