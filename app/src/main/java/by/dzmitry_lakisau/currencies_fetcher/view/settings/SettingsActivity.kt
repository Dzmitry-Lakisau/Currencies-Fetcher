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

class SettingsActivity : AppCompatActivity(), OnStartDragListener {

    private lateinit var itemTouchHelper: ItemTouchHelper

    private val settings: Settings by inject()

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    private val settingsAdapter = SettingsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        rv_currencies_settings.apply {
            layoutManager = LinearLayoutManager(this@SettingsActivity)
            adapter = settingsAdapter
        }

        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){
            override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                settingsAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        })
        itemTouchHelper.attachToRecyclerView(rv_currencies_settings)

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

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
