package fxc.dev.app.helper.event_tracking

import android.content.Context
import fxc.dev.fox_purchase.model.IAPProduct

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

interface EventTracking {
    fun startTrackingWith(
        context: Context,
        appFlyerId: String,
        adjustAppToken: String,
        adjustPurchaseToken: String
    )

    fun logPurchaseEvent(
        context: Context?,
        iapProduct: IAPProduct?
    )
}