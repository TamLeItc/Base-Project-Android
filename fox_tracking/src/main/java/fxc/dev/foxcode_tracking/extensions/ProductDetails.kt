package fxc.dev.foxcode_tracking.extensions

import com.android.billingclient.api.ProductDetails

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

fun ProductDetails.subscriptionOfferDetails(): ProductDetails.SubscriptionOfferDetails? {
    var biggestPricedOffer: ProductDetails.SubscriptionOfferDetails? = null
    var biggestPrice = 0

    this.subscriptionOfferDetails?.let {
        it.map { offer ->
            offer
        }
    }?.let {
        if (it.isNotEmpty()) {
            for (offer in it) {
                for (price in offer.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros > biggestPrice) {
                        biggestPrice = price.priceAmountMicros.toInt()
                        biggestPricedOffer = offer
                    }
                }
            }
        }
    }

    return biggestPricedOffer
}

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