package fxc.dev.fox_purchase.extension

import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.model.IAPProductType

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

fun List<IAPProduct>.filter(type: IAPProductType): List<IAPProduct> {
    return filter { it.productType == type }
}