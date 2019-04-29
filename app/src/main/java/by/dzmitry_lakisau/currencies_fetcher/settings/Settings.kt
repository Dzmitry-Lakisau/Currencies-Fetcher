package by.dzmitry_lakisau.currencies_fetcher.settings

import android.content.SharedPreferences
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class Settings(private val sharedPreferences: SharedPreferences) {

    companion object{
        private const val SETTINGS_KEY = "selected_currencies"
    }

    private val defaultSettings: LinkedHashMap<String, Boolean> by lazy {
        val defaultSettings = LinkedHashMap<String, Boolean>()
        defaultSettings["AUD"] = false
        defaultSettings["BGN"] = false
        defaultSettings["UAH"] = false
        defaultSettings["DKK"] = false
        defaultSettings["USD"] = true
        defaultSettings["EUR"] = true
        defaultSettings["PLN"] = false
        defaultSettings["IRR"] = false
        defaultSettings["ISK"] = false
        defaultSettings["JPY"] = false
        defaultSettings["CAD"] = false
        defaultSettings["CNY"] = false
        defaultSettings["KWD"] = false
        defaultSettings["MDL"] = false
        defaultSettings["NZD"] = false
        defaultSettings["NOK"] = false
        defaultSettings["RUB"] = true
        defaultSettings["XDR"] = false
        defaultSettings["SGD"] = false
        defaultSettings["KGS"] = false
        defaultSettings["KZT"] = false
        defaultSettings["TRY"] = false
        defaultSettings["GBP"] = false
        defaultSettings["CZK"] = false
        defaultSettings["SEK"] = false
        defaultSettings["CHF"] = false
        return@lazy defaultSettings
    }

    private val availableCurrencies = ArrayList<CurrencySetting>()

    private var selectedCurrencies: LinkedHashMap<String, Boolean>

    init {
        selectedCurrencies = load()
    }

    fun applyTo(unorderedCurrencies: List<CurrencyTwoDateRate>): List<CurrencyTwoDateRate>{

        updateAvailableCurrencies(unorderedCurrencies)
        val filteredCurrencies = LinkedList<CurrencyTwoDateRate>()
        selectedCurrencies.forEach { selection ->
            if (selection.value) {
                filteredCurrencies.add(
                    unorderedCurrencies.find { it.charCode == selection.key}!!
                )
            }
        }
        return filteredCurrencies
    }

    private fun updateAvailableCurrencies(unorderedCurrencies: List<CurrencyTwoDateRate>){
        availableCurrencies.clear()
        unorderedCurrencies.forEach {
            availableCurrencies.add(CurrencySetting(it.charCode, it.scale, it.name, selectedCurrencies[it.charCode] ?: false))
        }
    }

    fun save(orderedCurrencySettings: List<CurrencySetting>) {
        selectedCurrencies.clear()
        orderedCurrencySettings.forEach { selectedCurrencies[it.charCode] = it.selected }

        val jsonArray = JSONArray()
        selectedCurrencies.forEach{
            val jsonObject = JSONObject()
            jsonObject.put(it.key, it.value)
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString(SETTINGS_KEY, jsonArray.toString()).apply()
    }

    private fun load(): LinkedHashMap<String, Boolean> {
        val selectedCurrencies = LinkedHashMap<String, Boolean>()

        val settingsString = sharedPreferences.getString(SETTINGS_KEY, null)
        return if (settingsString != null){
            val jsonArray = JSONArray(settingsString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val key = jsonObject.keys().next()
                selectedCurrencies[key] = jsonObject.getBoolean(key)
            }
            selectedCurrencies
        }
        else defaultSettings
    }

    fun get(): List<CurrencySetting> {
        val sortedCurrencySettings = ArrayList<CurrencySetting>()
        selectedCurrencies.forEach { selection ->
            val currencySetting = availableCurrencies.find { it.charCode == selection.key}!!
            currencySetting.selected = selection.value
            sortedCurrencySettings.add(currencySetting)
        }
        return sortedCurrencySettings
    }
}
