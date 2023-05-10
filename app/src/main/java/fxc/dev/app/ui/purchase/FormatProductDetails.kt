package fxc.dev.app.ui.purchase

import android.content.Context
import com.android.billingclient.api.ProductDetails
import fxc.dev.app.R

fun ProductDetails.PricingPhase?.timePeriodPage(context: Context): String {
    return when (this?.billingPeriod) {
        "P1W" -> context.getString(R.string.weak)
        "P1M" -> context.getString(R.string.month)
        "P1Y" -> context.getString(R.string.year)
        else -> ""
    }
}

fun ProductDetails.PricingPhase?.namePeriodPage(context: Context): String {
    return when (this?.billingPeriod) {
        "P1W" -> context.getString(R.string.weakly)
        "P1M" -> context.getString(R.string.monthly)
        "P1Y" -> context.getString(R.string.yealy)
        else -> ""
    }
}

val ProductDetails.PricingPhase?.priceFormatted get() = this?.formattedPrice ?: ""


