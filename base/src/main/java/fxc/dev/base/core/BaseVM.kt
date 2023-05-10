package fxc.dev.base.core

import androidx.lifecycle.ViewModel
import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.core.domain.repository.LocalRepository
import fxc.dev.core.domain.repository.RemoteRepository
import fxc.dev.fox_ads.AdsHelperImp
import fxc.dev.fox_purchase.manager.PurchaseManager
import fxc.dev.fox_purchase.utils.PurchaseUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

abstract class BaseVM protected constructor() : ViewModel(), CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = dispatchers.io + SupervisorJob()

    protected val dispatchers: CoroutineDispatchers by inject()
    protected val adsHelper: AdsHelperImp by inject()
    protected val remoteRepository: RemoteRepository by inject()
    protected val localRepository: LocalRepository by inject()
    protected val purchaseManager: PurchaseManager by inject()

    protected val TAG = this.javaClass.simpleName

    val purchasedFlow: Flow<Boolean> =
        purchaseManager.getPurchasedListState()
            .map { it.isNotEmpty() }
            .map { PurchaseUtils.isPremium }
            .onStart { emit(PurchaseUtils.isPremium) }

    val nativeAdFlow =
        adsHelper.getNativeAdFlow()
            .combine(purchasedFlow) { ads, purchased ->
                if (purchased) emptyList() else ads
            }
}