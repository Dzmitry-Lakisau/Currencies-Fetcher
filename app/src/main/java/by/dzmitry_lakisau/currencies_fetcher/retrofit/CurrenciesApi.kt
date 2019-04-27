package by.dzmitry_lakisau.currencies_fetcher.retrofit

import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {
    @GET("XmlExRates.aspx")//TODO change to Date
    fun getExchangeRates(@Query("ondate") date: String? = null): Single<DailyExchangeRates>
}
