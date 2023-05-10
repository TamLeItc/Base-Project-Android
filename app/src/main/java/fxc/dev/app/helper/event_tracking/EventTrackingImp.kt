package fxc.dev.app.helper.event_tracking

import android.content.Context
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustEvent
import com.android.billingclient.api.BillingClient
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import fxc.dev.fox_purchase.BuildConfig
import fxc.dev.fox_purchase.extension.biggestPrice
import fxc.dev.fox_purchase.extension.subscriptionOfferDetails
import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.model.IAPProductType

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

class EventTrackingImp() : EventTracking {

    private val TAG = "LogEventHelper"

    private var isInitiated: Boolean = false

    private var adjustPurchaseToken = ""

    override fun startTrackingWith(
        context: Context,
        appFlyerId: String,
        adjustAppToken: String,
        adjustPurchaseToken: String
    ) {
        if (isInitiated) return

        this.adjustPurchaseToken = adjustPurchaseToken
        this.isInitiated = true

        initAdjust(context, adjustAppToken)
        initAppsFlyer(context, appFlyerId)
    }

    private fun initAdjust(
        context: Context,
        adjustAppToken: String
    ) {
        val env: String =
            if (BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(context, adjustAppToken, env)
        config.setLogLevel(com.adjust.sdk.LogLevel.VERBOSE)
        Adjust.onCreate(config)
    }

    private fun initAppsFlyer(
        context: Context,
        appFlyerId: String,
    ) {
        val appFlyer = AppsFlyerLib.getInstance()
        appFlyer.let {
            it.setDebugLog(BuildConfig.DEBUG)
            it.init(appFlyerId, null, context)
            it.start(context)
        }
    }

    override fun logPurchaseEvent(
        context: Context?,
        iapProduct: IAPProduct?
    ) {
        if (context == null || iapProduct == null) {
            return
        }

        val contentType = if (iapProduct.productType == IAPProductType.Subscription) {
            BillingClient.ProductType.SUBS
        } else {
            BillingClient.ProductType.INAPP
        }

        val productId = iapProduct.skuId

        iapProduct.productDetails?.let { productDetails ->
            productDetails.subscriptionOfferDetails()?.let { offerDetails ->
                offerDetails.biggestPrice()?.let { pricePhase ->
                    val revenue = pricePhase.priceAmountMicros / 1000000
                    val currencyCode = pricePhase.priceCurrencyCode

                    logToAppsFlyer(
                        context = context,
                        productId = productId,
                        revenue = revenue,
                        currencyCode = currencyCode,
                        contentType = contentType
                    )
                    logToAdjust(
                        purchaseToken = adjustPurchaseToken,
                        productId = productId,
                        revenue = revenue.toDouble(),
                        currencyCode = currencyCode
                    )
                    logToFirebaseAnalytics(
                        productId = productId,
                        revenue = revenue,
                        currencyCode = currencyCode,
                        contentType = contentType
                    )
                }
            }
        }
    }

    private fun logToAdjust(
        purchaseToken: String,
        productId: String,
        revenue: Double,
        currencyCode: String
    ) {
        val adjustEvent =
            AdjustEvent(purchaseToken)

        adjustEvent.setRevenue(
            revenue,
            currencyCode
        )
        adjustEvent.setOrderId(productId)
        Adjust.trackEvent(adjustEvent)
    }

    private fun logToAppsFlyer(
        context: Context,
        productId: String,
        revenue: Long,
        currencyCode: String,
        contentType: String,
    ) {


        AppsFlyerLib.getInstance().logEvent(
            context,
            AFInAppEventType.PURCHASE,
            mapOf(
                AFInAppEventParameterName.PRICE to revenue,
                AFInAppEventParameterName.CONTENT_TYPE to contentType,
                AFInAppEventParameterName.CONTENT_ID to productId,
                AFInAppEventParameterName.REVENUE to revenue,
                AFInAppEventParameterName.CURRENCY to currencyCode
            ),
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.d(TAG, "Log event to appsflyer success")
                }

                override fun onError(p0: Int, p1: String) {
                    Log.d(TAG, "Log event to appsflyer failed: $p1")
                }
            }
        )
    }


    private fun logToFirebaseAnalytics(
        productId: String,
        revenue: Long,
        currencyCode: String,
        contentType: String,
    ) {

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param("id", productId)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
            param(FirebaseAnalytics.Param.PRICE, revenue)
            param(FirebaseAnalytics.Param.CURRENCY, currencyCode)
        }
    }
}