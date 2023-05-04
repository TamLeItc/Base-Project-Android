package fxc.dev.fox_purchase.billing.utils

import fxc.dev.common.utils.PrefUtils

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

object BillingUtils {
    val canShowInApp: Boolean
        get() {
            return PrefUtils.isPremium || PrefUtils.isBackDoorUsed
        }
}