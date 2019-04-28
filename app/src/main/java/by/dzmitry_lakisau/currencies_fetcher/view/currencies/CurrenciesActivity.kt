package by.dzmitry_lakisau.currencies_fetcher.view.currencies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.databinding.ActivityCurrenciesBinding
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import by.dzmitry_lakisau.currencies_fetcher.view.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_currencies.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrenciesActivity : AppCompatActivity() {

    companion object{
        private const val SETTINGS_CODE = 55
    }

    private val currenciesViewModel: CurrenciesViewModel by viewModel()

    private lateinit var binding: ActivityCurrenciesBinding

    private val currenciesAdapter = CurrenciesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_currencies)
        binding.currenciesViewModel = currenciesViewModel
        binding.executePendingBindings()

        binding.rvCurrencies.apply {
            layoutManager = LinearLayoutManager(this@CurrenciesActivity)
            adapter = currenciesAdapter
        }

        currenciesViewModel.currencies.observe(this, Observer<List<CurrencyTwoDateRate>> { currenciesAdapter.replaceAll(it) })

        toolbar_settings.setOnClickListener { startActivityForResult(SettingsActivity.newIntent(this), SETTINGS_CODE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                currenciesViewModel.loadCurrencyRates()
            }
        }
    }
}
