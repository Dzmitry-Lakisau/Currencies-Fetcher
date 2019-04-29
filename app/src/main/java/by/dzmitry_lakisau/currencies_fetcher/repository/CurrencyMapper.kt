package by.dzmitry_lakisau.currencies_fetcher.repository

import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import by.dzmitry_lakisau.currencies_fetcher.model.xml.DailyExchangeRates
import kotlin.collections.ArrayList

class CurrencyMapper {

    private var dailyExchangeRatesList = ArrayList<DailyExchangeRates>()

    fun addDailyExchangeRatesList(dailyExchangeRates: DailyExchangeRates) {
        dailyExchangeRatesList.add(dailyExchangeRates)
    }

    fun getTwoDayRates(): List<CurrencyTwoDateRate> {
        val currencyTwoDayRateList = ArrayList<CurrencyTwoDateRate>()

        dailyExchangeRatesList.sortByDescending { it.date }

        val latterRatesList = dailyExchangeRatesList[0]
        val earlierRatesList = dailyExchangeRatesList[1]

        latterRatesList.currencies.forEachIndexed { index, element ->
            val currencyTwoDayRate = CurrencyTwoDateRate(element.id, element.numCode, element.charCode, element.scale,
                element.name, element.rate, earlierRatesList.currencies[index].rate, latterRatesList.date, earlierRatesList.date)

            currencyTwoDayRateList.add(currencyTwoDayRate)
        }

        dailyExchangeRatesList.clear()
        return currencyTwoDayRateList
    }
}
