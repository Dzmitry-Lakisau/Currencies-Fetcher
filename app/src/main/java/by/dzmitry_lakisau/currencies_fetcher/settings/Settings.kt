package by.dzmitry_lakisau.currencies_fetcher.settings

import android.content.SharedPreferences
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class Settings(private val sharedPreferences: SharedPreferences) {

    companion object{
        private const val SETTINGS_KEY = "selected_currencies"
        private const val DEFAULT_SETTINGS = "[USD, EUR, RUB]"
    }

    private var availableCurrencies = ArrayList<CurrencyTwoDateRate>()

    private var selectedCurrencies: LinkedList<String>

    init {
        selectedCurrencies = load()
    }

    //in case currencies will be added on backend
    fun applyTo(currencies: List<CurrencyTwoDateRate>): List<CurrencyTwoDateRate>{

        updateAvailableCurrencies(currencies)

        val filteredCurrencies = LinkedList<CurrencyTwoDateRate>()
        selectedCurrencies.forEach { key ->
            filteredCurrencies.add(currencies.find { it.charCode == key }!!)
        }
        return filteredCurrencies
    }

    private fun updateAvailableCurrencies(currencies: List<CurrencyTwoDateRate>){
        availableCurrencies.clear()
        availableCurrencies.addAll(currencies)
    }

    fun save(){
        sharedPreferences.edit().putString(SETTINGS_KEY, selectedCurrencies.toString()).apply()
    }

    private fun load(): LinkedList<String> {
        val selectedCurrencies = LinkedList<String>()
        val jsonArray = JSONArray(sharedPreferences.getString(SETTINGS_KEY, DEFAULT_SETTINGS))
        for (i in 0 until jsonArray.length()) {
            selectedCurrencies.add(jsonArray.get(i) as String)
        }
        return selectedCurrencies
    }
}
