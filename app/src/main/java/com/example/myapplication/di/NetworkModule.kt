package com.example.myapplication.di

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.remote.GitHubApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// GitHub REST v3 — trailing slash required for Retrofit baseUrl.
private const val GITHUB_API_BASE_URL = "https://api.github.com/"

/** Wire format (HTTP) only: Moshi, OkHttp, Retrofit, and the API interface. */
val networkModule = module {
    single {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
    single<GitHubApi> { get<Retrofit>().create(GitHubApi::class.java) }
}
