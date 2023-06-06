package fxc.dev.fox_purchase.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import fxc.dev.common.extension.joinBy
import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.fox_purchase.extension.filter
import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.model.IAPProductType
import fxc.dev.fox_purchase.utils.PurchaseUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

class PurchaseManagerImp(
    val dispatcher: CoroutineDispatchers
) : PurchaseManager, PurchasesUpdatedListener, BillingClientStateListener {

    private lateinit var billingClient: BillingClient

    private var iapProducts: List<IAPProduct> = emptyList()

    private val productOrderIds: Map<String, Int>
        get() = iapProducts.map { it.skuId }.withIndex()
            .associate { it.value to it.index }

    private val purchasedListState = MutableStateFlow<List<Purchase>>(emptyList())
    private val productListState = MutableStateFlow<List<IAPProduct>>(emptyList())
    private val productPurchasedState = MutableSharedFlow<IAPProduct>(extraBufferCapacity = 64)

    private val iapProductList = mutableListOf<IAPProduct>()

    private var currentBillingProduct: IAPProduct? = null

    private var wasRetriedConnect = false

    override fun initialize(
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
            querySubscriptionProductDetails(iapProducts.filter(IAPProductType.Subscription))
            queryInAppProductDetails(iapProducts.filter(IAPProductType.InApp))

            queryPurchasesAsync()
        }
    }

    override fun onBillingServiceDisconnected() {
        if (!wasRetriedConnect) {
            CoroutineScope(dispatcher.io).launch {
                billingClient.startConnection(this@PurchaseManagerImp)
                delay(3000)
            }
            wasRetriedConnect = true
        }
    }

    /**
     * [PurchasesUpdatedListener]
     * */

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        Log.d(TAG, "onPurchasesUpdated: Responsive code: ${result.responseCode}")
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    currentBillingProduct?.let { productPurchasedState.tryEmit(it) }

                    purchases.forEach {
                        handleNonConsumableProduct(it)
                    }

                    PurchaseUtils.isPremium = purchases.isNotEmpty()
                    purchasedListState.tryEmit(purchases)
                }
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                queryPurchasesAsync()
            }
        }
    }

    override fun buyBasePlan(activity: Activity, iapProduct: IAPProduct) {
        currentBillingProduct = iapProduct

        iapProduct.productDetails?.let {
            val offerToken = biggestPricedOfferToken(it)

            val billingParams = billingFlowParamsBuilder(
                productDetails = it,
                offerToken = offerToken
            )

            launchBillingFlow(activity, billingParams)
        }
    }

    override fun getPurchasedListState(): StateFlow<List<Purchase>> {
        return purchasedListState.asStateFlow()
    }

    override fun getProductListState(): StateFlow<List<IAPProduct>> {
        return productListState.asStateFlow()
    }

    override fun getProductPurchasedState(): SharedFlow<IAPProduct> {
        return productPurchasedState.asSharedFlow()
    }

    override fun isServerConnected(): Boolean {
        return billingClient.connectionState != BillingClient.ConnectionState.DISCONNECTED
    }

    private fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        if (!billingClient.isReady) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready")
        }

        billingClient.launchBillingFlow(activity, params)
    }

    private fun queryInAppProductDetails(productSubscriptions: List<IAPProduct>) {
        val paramsSubscription = QueryProductDetailsParams.newBuilder()
        val subscriptionProductList = productSubscriptions.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.skuId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        paramsSubscription.setProductList(subscriptionProductList).apply {
            billingClient.queryProductDetailsAsync(this.build()) { result, productDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "querySkuDetails: ${productDetailsList.count()}")
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
    }

    private fun querySubscriptionProductDetails(productInApps: List<IAPProduct>) {
        val paramsInApp = QueryProductDetailsParams.newBuilder()
        val inAppProductList = productInApps.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.skuId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        paramsInApp.setProductList(inAppProductList).apply {
            billingClient.queryProductDetailsAsync(this.build()) { result, productDetailsList ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "querySkuDetails: ${productDetailsList.count()}")
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

    private fun queryPurchasesAsync() {
        billingClient.run {
            queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { result, purchases ->
                handlePurchasesResult(result, purchases)
            }

            queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { result, purchases ->
                handlePurchasesResult(result, purchases)
            }
        }
    }

    private fun handlePurchasesResult(
        result: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach {
                if (!it.isAcknowledged) {
                    handleNonConsumableProduct(it)
                }
            }

            Log.d(TAG, "handleCheckHasPurchaseAcknowledged: ${purchases.size}")
            PurchaseUtils.isPremium = purchases.isNotEmpty()
            purchasedListState.tryEmit(purchases)
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
                        PurchaseUtils.isPremium = true
                    } else {
                        handleNonConsumableProduct(purchase)
                    }
                }
            } else {
                PurchaseUtils.isPremium = true
            }
        }
    }

    private fun submitProductList() {
        val sortedIapProductList = iapProductList
            .sortedBy { productOrderIds[it.skuId] }
            .distinctBy { it.skuId }

        productListState.tryEmit(sortedIapProductList)
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

    companion object {
        const val TAG = "BillingUtils"
    }

}