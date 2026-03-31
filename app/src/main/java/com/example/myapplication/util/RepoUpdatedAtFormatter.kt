package com.example.myapplication.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * GitHub sends `updated_at` in UTC ISO; we show a short local date for the list.
 * Uses SimpleDateFormat so it runs on minSdk 24 without Java 8 desugaring.
 */
object RepoUpdatedAtFormatter {

    private val utc: TimeZone = TimeZone.getTimeZone("UTC")
    private val displayLocale: Locale = Locale.ENGLISH
    private const val DisplayPattern = "dd MMM, yyyy"

    private val utcPatterns: Array<String> = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
    )

    fun format(isoTimestamp: String?): String? {
        val raw = isoTimestamp?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val date = parseUtc(raw) ?: return null
        return SimpleDateFormat(DisplayPattern, displayLocale).apply {
            timeZone = TimeZone.getDefault()
        }.format(date)
    }

    private fun parseUtc(raw: String): Date? {
        for (pattern in utcPatterns) {
            try {
                return SimpleDateFormat(pattern, Locale.US).apply { timeZone = utc }.parse(raw)
            } catch (_: ParseException) {
                continue
            }
        }
        return null
    }
}
