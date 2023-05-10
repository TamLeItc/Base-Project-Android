package fxc.dev.fox_purchase.model

import com.android.billingclient.api.ProductDetails

/**
 * Created by Thanh Quang on 8/24/21.
 */
enum class IAPProductType {
    InApp, Subscription
}

data class IAPProduct(
    val productType: IAPProductType,
    val name: String,
    val desc: String?,
    val skuId: String,
    var productDetails: ProductDetails? = null,
    var isItemSelected: Boolean = false
) {
    val isFreeTrial: Boolean
        get() {
            return productDetails?.subscriptionOfferDetails?.isNotEmpty() ?: false &&
                    (productDetails?.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.size
                        ?: 0) >= 1 &&
                    productDetails?.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(
                        0
                    )?.priceAmountMicros == 0L
        }

    val freeTrialDays: Int
        get() {
            return if (isFreeTrial) {
                when (productDetails?.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(
                    0
                )?.billingPeriod) {
                    "P1D" -> 1
                    "P3D" -> 3
                    "P7D" -> 7
                    else -> 0
                }
            } else {
                0
            }
        }
}