package fxc.dev.foxcode_tracking.event_tracking

import android.app.Application
import android.content.Context
import com.android.billingclient.api.ProductDetails

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

interface EventTracking {
    fun startTrackingWith(
        application: Application,
        appFlyerId: String
    )

    fun logPurchaseEvent(
        context: Context?,
        product: ProductDetails?
    )
}