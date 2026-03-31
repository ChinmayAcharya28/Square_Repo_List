package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.appModule
import com.example.myapplication.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/** Starts Koin before any UI (`networkModule` then `appModule`). */
class SquareReposApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SquareReposApplication)
            modules(networkModule, appModule)
        }
    }
}
