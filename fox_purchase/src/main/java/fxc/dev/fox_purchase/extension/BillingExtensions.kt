package fxc.dev.fox_purchase.extension

import android.content.Context
import com.android.billingclient.api.ProductDetails
import fxc.dev.fox_purchase.R

fun ProductDetails.SubscriptionOfferDetails.biggestPrice(): ProductDetails.PricingPhase? {
  var biggestPrice = 0
  var biggestPricingPhase: ProductDetails.PricingPhase? = null
  for (price in pricingPhases.pricingPhaseList) {
    if (price.priceAmountMicros > biggestPrice) {
      biggestPrice = price.priceAmountMicros.toInt()
      biggestPricingPhase = price
    }
  }

  return biggestPricingPhase
}

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


