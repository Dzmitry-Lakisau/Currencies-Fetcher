package by.dzmitry_lakisau.currencies_fetcher.view.settings

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.settings.CurrencySetting
import kotlinx.android.synthetic.main.item_currency.view.txt_charCode
import kotlinx.android.synthetic.main.item_currency.view.txt_scale_name
import kotlinx.android.synthetic.main.item_currency_setting.view.*
import java.util.*
import kotlin.collections.ArrayList

class SettingsAdapter(val onStartDragListener: OnStartDragListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM = 0
    }

    val currencySettings = ArrayList<CurrencySetting>()

    override fun getItemCount() = currencySettings.size

    override fun getItemViewType(position: Int) = ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CurrencySettingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency_setting, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CurrencySettingViewHolder).bind(currencySettings[position])
    }

    fun addAll(newCurrenciesSettings: List<CurrencySetting>){
        currencySettings.addAll(newCurrenciesSettings)
        notifyDataSetChanged()
    }

    fun onItemMove(sourcePosition: Int, targetPosition: Int) {
        Collections.swap(currencySettings, sourcePosition, targetPosition)
        notifyItemMoved(sourcePosition, targetPosition)
    }

    inner class CurrencySettingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.switch_show.setOnCheckedChangeListener{ _, isChecked ->
                currencySettings[adapterPosition].selected = isChecked
            }
            
            itemView.imv_reorder.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) { onStartDragListener.onStartDrag(this) }
                false
            }
        }

        fun bind(currencySetting: CurrencySetting) {
            itemView.apply {
                txt_charCode.text = currencySetting.charCode
                txt_scale_name.text = resources.getString(R.string.currency_scale_name, currencySetting.scale, currencySetting.name)
                switch_show.isChecked = currencySetting.selected
            }
        }
    }
}
