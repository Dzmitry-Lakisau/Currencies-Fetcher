package by.dzmitry_lakisau.currencies_fetcher.view.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.currencies_fetcher.R
import by.dzmitry_lakisau.currencies_fetcher.model.CurrencyTwoDateRate
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrenciesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM = 0
        private const val HEADER = 1
        private const val ERROR_HEADER = 2
    }

    private val currencies = ArrayList<CurrencyTwoDateRate?>()

    private var isHeaderAdded = false
    private var isErrorHeaderAdded = false

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && isHeaderAdded -> HEADER
            position == 0 && isErrorHeaderAdded -> ERROR_HEADER
            else -> ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER -> HeaderViewHolder(layoutInflater.inflate(R.layout.item_header, parent, false))
            ERROR_HEADER -> ErrorHeaderViewHolder(layoutInflater.inflate(R.layout.item_error_header, parent, false))
            else -> CurrencyViewHolder(layoutInflater.inflate(R.layout.item_currency, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
           ITEM -> (holder as CurrencyViewHolder).bind(currencies[position]!!)
           else -> {}
       }
    }

    fun addAll(newCurrencies: List<CurrencyTwoDateRate>){
        currencies.addAll(newCurrencies)
        notifyDataSetChanged()
    }

    fun addHeader() {
        isHeaderAdded = true
        currencies.add(null)
        notifyItemInserted(0)
    }

    private fun removeHeader() {
        if (isHeaderAdded) {
            val position = 0
            currencies.removeAt(position)
            notifyItemRemoved(position)
            isHeaderAdded = false
        }
    }

    fun addErrorHeader() {
        isErrorHeaderAdded = true
        currencies.add(null)
        notifyItemInserted(0)
    }

    private fun removeErrorHeader() {
        if (isErrorHeaderAdded) {
            val position = 0
            currencies.removeAt(position)
            notifyItemRemoved(position)
            isErrorHeaderAdded = false
        }
    }

    fun removeAllHeadersAndFooters() {
        removeHeader()
        removeErrorHeader()
    }

    fun removeAll() {
        currencies.clear()
        notifyDataSetChanged()
    }

    class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class ErrorHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class CurrencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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
