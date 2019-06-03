package by.dzmitry_lakisau.currencies_fetcher.di

import android.preference.PreferenceManager
import by.dzmitry_lakisau.currencies_fetcher.BuildConfig
import by.dzmitry_lakisau.currencies_fetcher.repository.Repository
import by.dzmitry_lakisau.currencies_fetcher.retrofit.CurrenciesApi
import by.dzmitry_lakisau.currencies_fetcher.settings.Settings
import by.dzmitry_lakisau.currencies_fetcher.view.currencies.CurrenciesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

val appModule = module {

    single<OkHttpClient> {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.build()
    }

    single<CurrenciesApi> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(get())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(CurrenciesApi::class.java)
    }

    single {
        Repository(get())
    }

    single {
        Settings(PreferenceManager.getDefaultSharedPreferences(androidContext()))
    }

    viewModel{ CurrenciesViewModel(get(), get()) }
}
