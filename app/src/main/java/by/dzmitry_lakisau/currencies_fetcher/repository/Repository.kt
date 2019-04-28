package by.dzmitry_lakisau.currencies_fetcher.repository

import by.dzmitry_lakisau.currencies_fetcher.DATE_PATTERN_FOR_API
import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import by.dzmitry_lakisau.currencies_fetcher.retrofit.CurrenciesApi
import io.reactivex.Observable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val currenciesApi: CurrenciesApi) {

    private val dates: List<Date>

    private val simpleDateFormatter = SimpleDateFormat(DATE_PATTERN_FOR_API, Locale.getDefault())

    init {
        val calendar = Calendar.getInstance()

        val today = calendar.time

        calendar.add(Calendar.DATE, 1)
        val tomorrow = calendar.time

        calendar.add(Calendar.DATE, -2)
        val yesterday = calendar.time

        dates = arrayListOf<Date>(tomorrow, today, yesterday)
    }

    fun getExchangeRates(): Observable<DailyExchangeRates> {
        return Observable.fromIterable(dates)
            .flatMapSingle(
                {item -> getRatesForCertainDate(item)}, true
            )
            .take(2)
//            .doOnSubscribe()//TODO

    }

    private fun getRatesForCertainDate(date: Date): Single<DailyExchangeRates> {
        return currenciesApi.getExchangeRates(simpleDateFormatter.format(date))
    }
}
