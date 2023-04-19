package fxc.dev.fox_ads.module

import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.AdsHelperImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

val foxAdsModule = module {
    singleOf(::AdsHelperImp) { bind<AdsHelper>() }
}