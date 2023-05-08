package fxc.dev.base.core

import androidx.lifecycle.ViewModel
import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.core.domain.repository.LocalRepository
import fxc.dev.core.domain.repository.RemoteRepository
import fxc.dev.fox_ads.AdsHelperImp
import fxc.dev.fox_purchase.billing.utils.BillingManager
import fxc.dev.fox_purchase.billing.utils.BillingUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    val jobVM: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.io + jobVM

    protected val dispatchers: CoroutineDispatchers by inject()
    protected val adsHelper: AdsHelperImp by inject()
    protected val remoteRepository: RemoteRepository by inject()
    protected val localRepository: LocalRepository by inject()

    protected val TAG = this.javaClass.simpleName

    val purchaseFlow: Flow<Boolean> = BillingManager.shared.billingPurchasedS
        .map { BillingUtils.canShowInApp }
        .onStart { emit(BillingUtils.canShowInApp) }

    val nativeAdFlow = adsHelper.getNativeAdFlow().combine(purchaseFlow) { ads, purchased ->
        if (purchased) emptyList() else ads
    }

    override fun onCleared() {
        super.onCleared()
        jobVM.cancel()
    }
}