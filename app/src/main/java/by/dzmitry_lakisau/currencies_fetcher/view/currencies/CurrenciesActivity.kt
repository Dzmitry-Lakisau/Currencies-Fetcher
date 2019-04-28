package by.dzmitry_lakisau.currencies_fetcher.view.currencies

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.dzmitry_lakisau.currencies_fetcher.DATE_PATTERN_FOR_UI
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.repository.CurrencyMapper
import by.dzmitry_lakisau.currencies_fetcher.repository.Repository
import by.dzmitry_lakisau.currencies_fetcher.settings.Settings
import by.dzmitry_lakisau.currencies_fetcher.view.settings.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_currencies.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class CurrenciesActivity : AppCompatActivity() {

    private val repository: Repository by inject()

    private val settings: Settings by inject()

    private val currenciesAdapter = CurrenciesAdapter()

    private val currencyMapper = CurrencyMapper()

    private val simpleDateFormatter = SimpleDateFormat(DATE_PATTERN_FOR_UI, Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)

        rv_currencies.apply {
            layoutManager = LinearLayoutManager(this@CurrenciesActivity)
            adapter = currenciesAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        loadCurrencyRates()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                startActivity(SettingsActivity.newIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadCurrencyRates() {
        currenciesAdapter.removeAllHeadersAndFooters()
        currenciesAdapter.removeAll()
        currenciesAdapter.addHeader()

        CompositeDisposable().add(
            repository.getExchangeRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        currencyMapper.addDailyExchangeRatesList(it)
                    },
                    {
                        currenciesAdapter.removeAllHeadersAndFooters()
                        currenciesAdapter.addErrorHeader()
                        frame_head.visibility = View.GONE
                        Log.e(this.toString(), it.message)
                    },
                    {
                        val res = currencyMapper.getTwoDayRates()

                        txt_earlierDate.text = simpleDateFormatter.format(res[0].earlierDate)
                        txt_latterDate.text = simpleDateFormatter.format(res[0].latterDate)
                        frame_head.visibility = View.VISIBLE
                        currenciesAdapter.removeAllHeadersAndFooters()
                        currenciesAdapter.addAll(settings.applyTo(res))
                    }
                )
        )
    }
}
