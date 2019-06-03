package by.dzmitry_lakisau.currencies_fetcher.retrofit

import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {
    @GET("XmlExRates.aspx")
    fun getExchangeRates(@Query("ondate") date: String? = null): Call<DailyExchangeRates>
}
