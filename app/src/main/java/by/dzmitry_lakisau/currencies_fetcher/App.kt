package by.dzmitry_lakisau.currencies_fetcher

import android.app.Application
import by.dzmitry_lakisau.currencies_fetcher.di.appModule
import org.koin.android.ext.android.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}
