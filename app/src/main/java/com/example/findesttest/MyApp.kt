package com.example.findesttest

import android.app.Application
import com.example.findesttest.di.networkModule
import com.example.findesttest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)

            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}