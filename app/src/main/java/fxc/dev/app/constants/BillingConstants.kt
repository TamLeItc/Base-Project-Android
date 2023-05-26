package fxc.dev.app.constants

import android.content.res.Resources
import fxc.dev.app.MainApplication
import fxc.dev.app.R
import fxc.dev.fox_purchase.model.IAPInfo
import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.model.IAPProductType

/**
 * Created by Tam Le on 22/11/2021.
 */

object BillingConstants {
    private val resources: Resources
        get() = MainApplication.instance.resources

    private val SUB_YEAR =
        resources.getString(R.string.billing_sub_year) // Subscription ID

    private val SUB_MONTH =
        resources.getString(R.string.billing_sub_month) // Subscription ID

    private val ONETIME =
        resources.getString(R.string.billing_inapp_onetime) // Onetime non-consumable ID

    val iapInfoList: List<IAPInfo> = listOf(
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_1),
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_2),
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_3)
    )

    val productList = listOf(
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