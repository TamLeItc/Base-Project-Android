package fxc.dev.foxcode_tracking.event_tracking

import android.app.Application
import android.content.Context
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustEvent
import com.android.billingclient.api.ProductDetails
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import fxc.dev.dji_drone.common.remote_config.RemoteConfigKey
import fxc.dev.fox_tracking.BuildConfig
import fxc.dev.foxcode_tracking.extensions.biggestPrice
import fxc.dev.foxcode_tracking.extensions.subscriptionOfferDetails
import fxc.dev.foxcode_tracking.lifecycle.TrackingLifecycle
import fxc.dev.foxcode_tracking.remote_config.RemoteConfigManager

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

class EventTrackingImp : EventTracking {

    private val TAG = "LogEventHelper"

    private var isInitiated: Boolean = false

    override fun startTrackingWith(application: Application, appFlyerId: String) {
        if (isInitiated) return

        this.isInitiated = true

        initAdjust(application)
        initAppsFlyer(application, appFlyerId)

        TrackingLifecycle.getInstance().setRegisterLifecycleCallbacks(application)
    }

    private fun initAdjust(
        context: Context
    ) {
        val env: String =
            if (BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(context, RemoteConfigManager.getString(RemoteConfigKey.ADJUST_APP_TOKEN), env)
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
        product: ProductDetails?
    ) {
        if (context == null || product == null) {
            return
        }

        product.subscriptionOfferDetails()?.let { offerDetails ->
            offerDetails.biggestPrice()?.let { pricePhase ->
                val revenue = pricePhase.priceAmountMicros / 1000000
                val currencyCode = pricePhase.priceCurrencyCode

                logToAppsFlyer(
                    context = context,
                    productId = product.productId,
                    revenue = revenue,
                    currencyCode = currencyCode,
                    contentType = product.productType
                )
                logToAdjust(
                    purchaseToken = RemoteConfigManager.getString(RemoteConfigKey.ADJUST_PURCHASE_TOKEN),
                    productId = product.productId,
                    revenue = revenue.toDouble(),
                    currencyCode = currencyCode
                )
                logToFirebaseAnalytics(
                    productId = product.productId,
                    revenue = revenue,
                    currencyCode = currencyCode,
                    contentType = product.productType
                )
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