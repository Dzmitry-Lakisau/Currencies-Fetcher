package by.dzmitry_lakisau.currencies_fetcher.repository

import by.dzmitry_lakisau.currencies_fetcher.DATE_PATTERN_FOR_API
import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import by.dzmitry_lakisau.currencies_fetcher.retrofit.CurrenciesApi
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.simpleframework.xml.core.ValueRequiredException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*

class Repository(private val currenciesApi: CurrenciesApi) {

    private val tomorrow: Date
    private val today: Date
    private val yesterday: Date

    private val simpleDateFormatter = SimpleDateFormat(DATE_PATTERN_FOR_API, Locale.getDefault())

    init {
        val calendar = Calendar.getInstance()

        today = calendar.time

        tomorrow = calendar.apply { add(Calendar.DATE, 1) }.time

        yesterday = calendar.apply { add(Calendar.DATE, -2) }.time
    }

    fun getExchangeRates(): Observable<DailyExchangeRates> {
        return Observable.create<DailyExchangeRates> { emitter ->
            getTomorrowRates()
                .subscribe({ first ->
                    emitter.onNext(first)

                    getTodayOrYesterdayRates(today)
                        .subscribe({ second ->
                            emitter.onNext(second)
                            emitter.onComplete()
                        },
                            { emitter.onError(it) }
                        )
                }, {
                    emitter.onError(it)
                }, {
                    Observable.fromIterable(listOf(today, yesterday))
                        .flatMapSingle { getTodayOrYesterdayRates(it) }
                        .subscribe(
                            {
                                emitter.onNext(it)
                            }, {
                                emitter.onError(it)
                            }, {
                                emitter.onComplete()
                            }
                        )
                })
        }
    }

    private fun getTodayOrYesterdayRates(date: Date): Single<DailyExchangeRates> {
        return Single.create<DailyExchangeRates> { emitter ->
            try {
                val response = currenciesApi.getExchangeRates(simpleDateFormatter.format(date)).execute()
                if (response.isSuccessful) {
                    emitter.onSuccess(response.body()!!)
                } else {
                    emitter.tryOnError(ConnectException("Server responded with ${response.raw().code()}"))
                }
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun getTomorrowRates(): Maybe<DailyExchangeRates> {
        return Maybe.create<DailyExchangeRates> { emitter ->
            try {
                val response = currenciesApi.getExchangeRates(simpleDateFormatter.format(tomorrow)).execute()
                if (response.isSuccessful) {
                    emitter.onSuccess(response.body()!!)
                } else {
                    emitter.tryOnError(ConnectException("Server responded with ${response.raw().code()}"))
                }
            } catch (e: Exception) {
                if (e.cause is ValueRequiredException) {
                    emitter.onComplete()
                } else {
                    emitter.tryOnError(e)
                }
            }
        }.subscribeOn(Schedulers.io())
    }
}
