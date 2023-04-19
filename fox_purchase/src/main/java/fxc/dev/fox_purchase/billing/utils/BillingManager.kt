package fxc.dev.fox_purchase.billing.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.fxc.foxcode_framework.extension.joinBy
import fxc.dev.common.utils.PrefUtils
import fxc.dev.fox_purchase.billing.model.IAPInfo
import fxc.dev.fox_purchase.billing.model.IAPProduct
import fxc.dev.fox_purchase.billing.model.IAPProductType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

interface BillingUtilsDelegate {
    fun iapInfo(): List<IAPInfo>
    fun productList(): List<IAPProduct>
}

/**
 * Created by Thanh Quang on 8/23/21.
 */
class BillingManager : PurchasesUpdatedListener, BillingClientStateListener {
    companion object {
        var shared = BillingManager()

        const val TAG = "BillingUtils"
    }

    private lateinit var billingClient: BillingClient

    val serverIsConnected: Boolean
        get() = billingClient.connectionState != BillingClient.ConnectionState.DISCONNECTED

    var iapProducts: List<IAPProduct> = emptyList()

    private val productSubscriptions: List<IAPProduct>
        get() = iapProducts.filter { it.productType == IAPProductType.Subscription }

    private val productInApps: List<IAPProduct>
        get() = iapProducts.filter { it.productType == IAPProductType.InApp }

    private val productOrderIds: Map<String, Int>
        get() = iapProducts.map { it.skuId }.withIndex()
            .associate { it.value to it.index }

    private val iapProductList = mutableListOf<IAPProduct>()
    private val _iapProductListS = MutableStateFlow<List<IAPProduct>>(emptyList())
    val iapProductListS = _iapProductListS
        .asStateFlow()

    private val _billingPurchasedS = MutableSharedFlow<Boolean>(extraBufferCapacity = 64)
    val billingPurchasedS = _billingPurchasedS.asSharedFlow()

    private val purchaseList = mutableListOf<Purchase>()

    fun initialize(
        context: Context,
        iapProducts: List<IAPProduct>
    ) {
        this.iapProducts = iapProducts
        billingClient = BillingClient
            .newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(this)
    }

    /**
     * [BillingClientStateListener]
     * */

    override fun onBillingSetupFinished(result: BillingResult) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            querySkuDetails()
            checkHasPurchaseAcknowledged()
        }
    }

    override fun onBillingServiceDisconnected() {
        billingClient.startConnection(this)
    }

    /**
     * [PurchasesUpdatedListener]
     * */

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        Log.d(TAG, "onPurchasesUpdated: Responsive code: ${result.responseCode}")
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    PrefUtils.isPremium = true
                    _billingPurchasedS.tryEmit(PrefUtils.isPremium)

                    purchases.forEach {
                        handleNonConsumableProduct(it)
                    }
                }
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                PrefUtils.isPremium = true
                _billingPurchasedS.tryEmit(PrefUtils.isPremium)

                checkHasPurchaseAcknowledged()
            }
        }
    }

    private fun checkHasPurchaseAcknowledged() {
        billingClient.run {
            queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { result, purchases ->
                handleCheckHasPurchaseAcknowledged(result, purchases)
            }

            queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { result, purchases ->
                handleCheckHasPurchaseAcknowledged(result, purchases)
            }
        }
    }

    private fun handleCheckHasPurchaseAcknowledged(
        result: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchaseList.addAll(purchases)

            purchases.forEach {
                if (!it.isAcknowledged) {
                    handleNonConsumableProduct(it)
                }
            }

            Log.d(TAG, "handleCheckHasPurchaseAcknowledged: ${purchases.size}")
            PrefUtils.isPremium = purchaseList.isNotEmpty()
            _billingPurchasedS.tryEmit(PrefUtils.isPremium)
        }
    }

    private fun handleNonConsumableProduct(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgedPurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgedPurchaseParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        PrefUtils.isPremium = true
                    } else {
                        handleNonConsumableProduct(purchase)
                    }
                }
            } else {
                PrefUtils.isPremium = true
            }
        }
    }

    private fun querySkuDetails() {
        // Query subscription
        val paramsSubscription = QueryProductDetailsParams.newBuilder()
        val subscriptionProductList = productSubscriptions.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.skuId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        paramsSubscription.setProductList(subscriptionProductList).apply {
            billingClient.queryProductDetailsAsync(this.build()) { result, productDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "querySkuDetails: $productDetailsList")
                    val productWithSkuDetails = productSubscriptions
                        .joinBy(productDetailsList) { it.first.skuId == it.second.productId }
                        .map {
                            it.first.productDetails = it.second
                            it.first
                        }

                    iapProductList.addAll(productWithSkuDetails)
                    submitProductList()
                } else {
                    Log.d(TAG, "querySkuDetails: failed")
                }
            }
        }

        // Query in app
        val paramsInApp = QueryProductDetailsParams.newBuilder()
        val inAppProductList = productInApps.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.skuId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        paramsInApp.setProductList(inAppProductList).apply {
            billingClient.queryProductDetailsAsync(this.build()) { result, productDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "querySkuDetails: $productDetailsList")
                    val productWithSkuDetails = productInApps
                        .joinBy(productDetailsList) { it.first.skuId == it.second.productId }
                        .map {
                            it.first.productDetails = it.second
                            it.first
                        }

                    iapProductList.addAll(productWithSkuDetails)
                    submitProductList()
                } else {
                    Log.d(TAG, "querySkuDetails: failed")
                }
            }
        }
    }

    private fun submitProductList() {
        val sortedIapProductList = iapProductList
            .sortedBy { productOrderIds[it.skuId] }
            .distinctBy { it.skuId }

        _iapProductListS.tryEmit(sortedIapProductList)
    }

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        if (!billingClient.isReady) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready")
        }

        billingClient.launchBillingFlow(activity, params)
    }

    fun buyBasePlan(activity: Activity, productDetails: ProductDetails) {
        val offerToken = biggestPricedOfferToken(productDetails)

        val billingParams = billingFlowParamsBuilder(
            productDetails = productDetails,
            offerToken = offerToken
        )

        launchBillingFlow(activity, billingParams)
    }

    private fun biggestPricedOfferToken(
        productDetails: ProductDetails
    ): String {
        var offerToken = String()
        var biggestPricedOffer: ProductDetails.SubscriptionOfferDetails
        var biggestPrice = 0

        productDetails.subscriptionOfferDetails?.let {
            it.map { offer ->
                offer
            }
        }?.let {
            if (!it.isNullOrEmpty()) {
                for (offer in it) {
                    for (price in offer.pricingPhases.pricingPhaseList) {
                        if (price.priceAmountMicros > biggestPrice) {
                            biggestPrice = price.priceAmountMicros.toInt()
                            biggestPricedOffer = offer
                            offerToken = biggestPricedOffer.offerToken
                        }
                    }
                }
            }
        }

        return offerToken
    }

    fun biggestSubscriptionOfferDetailsToken(
        productDetails: ProductDetails
    ): ProductDetails.SubscriptionOfferDetails? {
        var biggestPricedOffer: ProductDetails.SubscriptionOfferDetails? = null
        var biggestPrice = 0

        productDetails.subscriptionOfferDetails?.let {
            it.map { offer ->
                offer
            }
        }?.let {
            if (!it.isNullOrEmpty()) {
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

    private fun billingFlowParamsBuilder(productDetails: ProductDetails, offerToken: String):
            BillingFlowParams {
        return BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
        ).build()
    }

}
