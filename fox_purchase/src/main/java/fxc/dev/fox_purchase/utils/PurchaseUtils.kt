package fxc.dev.fox_purchase.utils

import com.chibatching.kotpref.KotprefModel

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

object PurchaseUtils : KotprefModel() {
    var isPremium by booleanPref(false)
}