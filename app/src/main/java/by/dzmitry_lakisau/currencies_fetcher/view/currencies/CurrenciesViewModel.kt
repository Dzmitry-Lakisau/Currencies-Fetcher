package by.dzmitry_lakisau.currencies_fetcher.view.currencies

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.dzmitry_lakisau.currencies_fetcher.DATE_PATTERN_FOR_UI
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import by.dzmitry_lakisau.currencies_fetcher.repository.CurrencyMapper
import by.dzmitry_lakisau.currencies_fetcher.repository.Repository
import by.dzmitry_lakisau.currencies_fetcher.settings.Settings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class CurrenciesViewModel(private val repository: Repository, val settings: Settings): ViewModel() {

    private val currencyMapper = CurrencyMapper()

    private val simpleDateFormatter = SimpleDateFormat(DATE_PATTERN_FOR_UI, Locale.getDefault())

    private val compositeDisposable = CompositeDisposable()

    val latterDate = ObservableField<String>()
    val earlierDate = ObservableField<String>()

    val isLoading = ObservableField(true)
    val isError = ObservableField(false)
    val isSuccess = ObservableField(false)

    val currencies = MutableLiveData<List<CurrencyTwoDateRate>>()

    init {
        loadCurrencyRates()
    }

    fun loadCurrencyRates() {
        isLoading.set(true)
        isError.set(false)
        isSuccess.set(false)

        compositeDisposable.add(
            repository.getExchangeRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        currencyMapper.addDailyExchangeRatesList(it)
                    },
                    {
                        isLoading.set(false)
                        isError.set(true)
                        isSuccess.set(false)
                    },
                    {
                        isLoading.set(false)
                        isError.set(false)
                        isSuccess.set(true)

                        currencies.value = settings.applyTo(currencyMapper.getTwoDayRates())

                        earlierDate.set(simpleDateFormatter.format(currencies.value!!.first().earlierDate))
                        latterDate.set(simpleDateFormatter.format(currencies.value!!.first().latterDate))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
