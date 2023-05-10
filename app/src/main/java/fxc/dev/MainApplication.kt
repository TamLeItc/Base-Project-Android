package fxc.dev

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import com.google.firebase.FirebaseApp
import fxc.dev.app.BuildConfig
import fxc.dev.app.R
import fxc.dev.app.constants.BillingConstants
import fxc.dev.app.helper.LifecycleManager
import fxc.dev.app.helper.event_tracking.EventTracking
import fxc.dev.app.module.appModules
import fxc.dev.common.remote_config.RemoteConfigManager
import fxc.dev.dji_drone.common.remote_config.RemoteConfigKey
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_purchase.manager.PurchaseManager
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

class MainApplication : Application(), KoinComponent {

    private val adsHelper: AdsHelper by inject()
    private val purchaseManager: PurchaseManager by inject()
    private val lifecycleManager: LifecycleManager by inject()
    private val remoteConfig: RemoteConfigManager by inject()
    private val eventTracking: EventTracking by inject()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        instance = this

        startKoin()

        initBilling()
        initAdvertisement()
        firebaseConfigs()

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

    private fun initAdvertisement() {
        adsHelper.initialize(
            delayShowInterstitialAd = resources.getInteger(R.integer.delay_show_interstitial_ad),
            adsOpenId = getString(R.string.ads_open_ads_id),
            adsBannerId = getString(R.string.ads_banner_id),
            adsNativeId = getString(R.string.ads_native_id),
            adsInterstitialId = getString(R.string.ads_interstitial_id)
        )
    }

    private fun initBilling() {
        purchaseManager.initialize(
            context = this,
            iapProducts = BillingConstants.productList
        )
    }

    private fun firebaseConfigs() {
        FirebaseApp.initializeApp(this)

        remoteConfig.onCreate(this)
        remoteConfig.startFetchConfig { config ->
            initEventTracking(
                adjustAppToken = config.getString(RemoteConfigKey.ADJUST_APP_TOKEN),
                adjustPurchaseToken = config.getString(RemoteConfigKey.ADJUST_PURCHASE_TOKEN)
            )
        }
    }

    private fun initEventTracking(
        adjustAppToken: String,
        adjustPurchaseToken: String,
    ) {
        eventTracking.startTrackingWith(
            context = this,
            appFlyerId = getString(R.string.app_flyer_id),
            adjustAppToken = adjustAppToken,
            adjustPurchaseToken = adjustPurchaseToken
        )
    }

    companion object {
        lateinit var instance: MainApplication
    }
}