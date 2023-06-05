package fxc.dev.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import com.google.firebase.FirebaseApp
import fxc.dev.app.constants.BillingConstants
import fxc.dev.app.helper.lifecycle.LifecycleManager
import fxc.dev.app.module.appModules
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_purchase.manager.PurchaseManager
import fxc.dev.foxcode_tracking.event_tracking.EventTracking
import fxc.dev.foxcode_tracking.remote_config.RemoteConfigManager
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

    private val purchaseManager: PurchaseManager by inject()
    private val eventTracking: EventTracking by inject()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        instance = this

        startKoin()

        LifecycleManager.getInstance().initialize()

        initBilling()
        initAdvertisement()
        firebaseConfigs()

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
        AdsHelper.getInstance().initialize(
            application = this,
            delayShowInterstitialAd = resources.getInteger(R.integer.delay_show_interstitial_ad),
            adsOpenId = getString(R.string.ads_open_ads_id),
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

        RemoteConfigManager.onCreate()
        RemoteConfigManager.startFetchConfig {
            startEventTracking()
        }
    }

    private fun startEventTracking() {
        eventTracking.startTrackingWith(
            application = this,
            appFlyerId = getString(R.string.app_flyer_id)
        )
    }

    companion object {
        lateinit var instance: MainApplication
    }
}