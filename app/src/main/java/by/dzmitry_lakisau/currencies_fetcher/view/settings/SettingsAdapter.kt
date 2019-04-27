package by.dzmitry_lakisau.currencies_fetcher.view.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import kotlinx.android.synthetic.main.item_currency.view.*

class SettingsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM = 0
    }

    private val currencies = ArrayList<CurrencyTwoDateRate?>()

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CurrencySettingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CurrencySettingViewHolder).bind(currencies[position]!!)
    }

    fun addAll(newCurrencies: List<CurrencyTwoDateRate>){
        currencies.addAll(newCurrencies)
        notifyDataSetChanged()
    }

    class CurrencySettingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(currencyTwoDateRate: CurrencyTwoDateRate) {
            itemView.apply {
                txt_charCode.text = currencyTwoDateRate.charCode
                txt_scale_name.text = resources.getString(R.string.currency_scale_name, currencyTwoDateRate.scale, currencyTwoDateRate.name)
                txt_earlierDateRate.text = currencyTwoDateRate.earlierDateRate.toString()
                txt_latterDateRate.text = currencyTwoDateRate.latterDateRate.toString()
            }
        }
    }
}
