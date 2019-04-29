package by.dzmitry_lakisau.currencies_fetcher.model

import java.math.BigDecimal
import java.util.*

data class CurrencyTwoDateRate(

    val id: Int,

    val numCode: String,

    val charCode: String,

    val scale: Int,

    val name: String,

    val latterDateRate: BigDecimal,

    val earlierDateRate: BigDecimal,

    val latterDate: Date,

    val earlierDate: Date
)
