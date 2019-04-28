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
            val currencyTwoDayRate = CurrencyTwoDateRate()
            currencyTwoDayRate.apply {
                scale = element.scale
                charCode = element.charCode
                id = element.id
                numCode = element.numCode
                name = element.name
                latterDateRate = element.rate
                earlierDateRate = earlierRatesList.currencies[index].rate
                latterDate = latterRatesList.date
                earlierDate = earlierRatesList.date
            }
            currencyTwoDayRateList.add(currencyTwoDayRate)
        }

        dailyExchangeRatesList.clear()
        return currencyTwoDayRateList
    }
}
