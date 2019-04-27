package by.dzmitry_lakisau.currencies_fetcher.repository

import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import by.dzmitry_lakisau.currencies_fetcher.retrofit.CurrenciesApi
import io.reactivex.Observable
import io.reactivex.Single

class Repository(private val currenciesApi: CurrenciesApi) {

    fun getExchangeRates(dates: List<String>): Observable<DailyExchangeRates> {
        return Observable.fromIterable(dates)
            .flatMapSingle(
                {item -> getRatesForCertainDate(item)}, true
            )
            .take(2)
//            .doOnSubscribe()//TODO

    }

    private fun getRatesForCertainDate(date: String): Single<DailyExchangeRates> {
        return currenciesApi.getExchangeRates(date)
    }
}
