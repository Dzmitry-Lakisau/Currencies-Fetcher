package by.dzmitry_lakisau.currencies_fetcher.view.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.settings.Settings
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.android.ext.android.inject

class SettingsActivity : AppCompatActivity() {

    private val settings: Settings by inject()

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    private val settingsAdapter = SettingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        rv_currencies_settings.apply {
            layoutManager = LinearLayoutManager(this@SettingsActivity)
            adapter = settingsAdapter
        }

        settingsAdapter.dragHelper.attachToRecyclerView(rv_currencies_settings)

        toolbar_back.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        toolbar_done.setOnClickListener {
            settings.save(settingsAdapter.currencySettings)
            setResult(Activity.RESULT_OK)
            finish()
        }

        settingsAdapter.addAll(settings.get())
    }
}
