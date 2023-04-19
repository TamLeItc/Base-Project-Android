package fxc.dev.app.module

import fxc.dev.common.module.commonModule
import fxc.dev.core.module.databaseModule
import fxc.dev.core.module.networkModule
import fxc.dev.core.module.repositoryModule
import fxc.dev.fox_ads.module.foxAdsModule

/**
 *
 * Created by tamle on 17/04/2023
 *
 */

val appModules = listOf(
    commonModule,
    networkModule,
    databaseModule,
    repositoryModule,
    viewModelModule,
    foxAdsModule,
    lifecycleModule,
    navigatorModule
)