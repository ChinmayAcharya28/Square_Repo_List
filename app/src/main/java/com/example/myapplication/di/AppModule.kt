package com.example.myapplication.di

import com.example.myapplication.data.DefaultRepoRepository
import com.example.myapplication.data.RepoRepository
import com.example.myapplication.ui.repositories.RepositoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** App-level wiring: repository + screen ViewModel (load `networkModule` first in Application). */
val appModule = module {
    single<RepoRepository> { DefaultRepoRepository(get()) }
    viewModel { RepositoriesViewModel(get()) }
}
