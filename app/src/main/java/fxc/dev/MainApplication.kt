package fxc.dev

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import fxc.dev.app.BuildConfig
import fxc.dev.app.R
import fxc.dev.app.constants.BillingConstants
import fxc.dev.app.lifecycle.LifecycleManager
import fxc.dev.app.module.appModules
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_purchase.billing.utils.BillingManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 *
 * Created by tamle on 17/04/2023
 *
 */

class MainApplication: Application(), KoinComponent {

    private val lifecycleManager: LifecycleManager by inject()
    private val adsHelper: AdsHelper by inject()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        instance = this

        startKoin()
        initAdsAndBilling()
        lifecycleManager.initialize()
        Kotpref.init(this)
    }

    private fun startKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(instance)
            modules(appModules)
        }
    }

    private fun initAdsAndBilling() {
        adsHelper.initialize(
            delayShowInterstitialAd = resources.getInteger(R.integer.delay_show_interstitial_ad),
            adsOpenId = getString(R.string.ads_open_ads_id),
            adsBannerId = getString(R.string.ads_banner_id),
            adsNativeId = getString(R.string.ads_native_id),
            adsInterstitialId = getString(R.string.ads_interstitial_id)
        )

        BillingManager.shared.initialize(
            context = instance,
            iapProducts = BillingConstants.productList
        )
    }

    companion object {
        lateinit var instance: MainApplication
    }
}