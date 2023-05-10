package fxc.dev.fox_purchase.manager

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.Purchase
import fxc.dev.fox_purchase.model.IAPProduct
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

interface PurchaseManager {
    fun initialize(
        context: Context,
        iapProducts: List<IAPProduct>
    )

    fun buyBasePlan(activity: Activity, iapProduct: IAPProduct)

    fun getPurchasedListState(): StateFlow<List<Purchase>>

    fun getProductListState(): StateFlow<List<IAPProduct>>

    fun getProductPurchasedState(): SharedFlow<IAPProduct>

    fun isServerConnected(): Boolean
}