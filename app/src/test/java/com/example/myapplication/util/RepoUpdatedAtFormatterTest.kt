package com.example.myapplication.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.TimeZone

class RepoUpdatedAtFormatterTest {

    @Test
    fun format_nullOrBlank_returnsNull() {
        assertNull(RepoUpdatedAtFormatter.format(null))
        assertNull(RepoUpdatedAtFormatter.format(""))
        assertNull(RepoUpdatedAtFormatter.format("   "))
    }

    @Test
    fun format_githubIsoNoMillis_utcDisplay() {
        val previousTz = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            assertEquals(
                "26 Jan, 2011",
                RepoUpdatedAtFormatter.format("2011-01-26T19:06:43Z"),
            )
        } finally {
            TimeZone.setDefault(previousTz)
        }
    }

    @Test
    fun format_githubIsoWithMillis() {
        val previousTz = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            assertEquals(
                "15 Mar, 2024",
                RepoUpdatedAtFormatter.format("2024-03-15T10:30:00.000Z"),
            )
        } finally {
            TimeZone.setDefault(previousTz)
        }
    }
}
