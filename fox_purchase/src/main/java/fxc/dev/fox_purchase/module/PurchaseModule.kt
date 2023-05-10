package fxc.dev.fox_purchase.module

import fxc.dev.fox_purchase.manager.PurchaseManager
import fxc.dev.fox_purchase.manager.PurchaseManagerImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 09/05/2023
 *
 */
 
val purchaseModule = module {
    singleOf(::PurchaseManagerImp) { bind<PurchaseManager>() }
}