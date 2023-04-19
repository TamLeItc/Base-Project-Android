package fxc.dev.app.constants

import android.content.res.Resources
import fxc.dev.MainApplication
import fxc.dev.fox_purchase.R
import fxc.dev.fox_purchase.billing.model.IAPProduct
import fxc.dev.fox_purchase.billing.model.IAPProductType

/**
 * Created by Tam Le on 22/11/2021.
 */

object BillingConstants : fxc.dev.fox_purchase.billing.utils.BillingConstants() {
    private val resources: Resources
        get() = MainApplication.instance.resources

    private val SUB_YEAR =
        resources.getString(fxc.dev.app.R.string.billing_sub_year) // Subscription ID

    private val SUB_MONTH =
        resources.getString(fxc.dev.app.R.string.billing_sub_month) // Subscription ID

    private val ONETIME =
        resources.getString(fxc.dev.app.R.string.billing_inapp_onetime) // Onetime non-consumable ID

    override val productList = listOf(
        IAPProduct(
            productType = IAPProductType.Subscription,
            name = MainApplication.instance.getString(R.string.yealy),
            desc = MainApplication.instance.getString(R.string.save10),
            skuId = SUB_YEAR
        ),
        IAPProduct(
            productType = IAPProductType.Subscription,
            name = MainApplication.instance.getString(R.string.monthly),
            desc = null,
            skuId = SUB_MONTH
        ),
        IAPProduct(
            productType = IAPProductType.InApp,
            name = MainApplication.instance.getString(R.string.onetime),
            desc = MainApplication.instance.getString(R.string.limited_time_offer),
            skuId = ONETIME
        )
    )
}