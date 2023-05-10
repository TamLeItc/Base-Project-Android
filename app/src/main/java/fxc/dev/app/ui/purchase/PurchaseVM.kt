package fxc.dev.app.ui.purchase

import android.app.Activity
import androidx.lifecycle.viewModelScope
import fxc.dev.MainApplication
import fxc.dev.app.constants.BillingConstants.iapInfoList
import fxc.dev.app.helper.event_tracking.EventTracking
import fxc.dev.base.core.BaseVM
import fxc.dev.fox_purchase.model.IAPInfo
import fxc.dev.fox_purchase.model.IAPProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

class PurchaseVM : BaseVM() {

    private val eventTracking: EventTracking by inject()

    private var _purchaseState: MutableStateFlow<PurchaseState> = MutableStateFlow(PurchaseState.Init)
    val purchaseState = _purchaseState.asStateFlow()

    private var _infoPremiumState: MutableStateFlow<List<IAPInfo>> = MutableStateFlow(emptyList())
    val infoPremiumState = _infoPremiumState.asStateFlow()

    private val selectedIndexFlow = MutableStateFlow(0)

    private var selectedProduct: IAPProduct? = null

    init {
        purchaseManager.getProductPurchasedState()
            .onEach {
                eventTracking.logPurchaseEvent(MainApplication.instance, it)
            }.launchIn(viewModelScope)
    }

    fun fetchData() {
        _infoPremiumState.tryEmit(iapInfoList)

        if (!purchaseManager.isServerConnected()) {
            _purchaseState.tryEmit(PurchaseState.ConnectingPlayStore)
        }

        purchaseManager.getProductListState()
            .combine(selectedIndexFlow) { items, selectedIndex->
                items.forEachIndexed { index, iapProduct ->
                    iapProduct.isItemSelected = selectedIndex == index
                }
                items
            }.onEach { list ->
                selectedProduct = list.firstOrNull { it.isItemSelected }
                _purchaseState.tryEmit(PurchaseState.ListProductUpdated(list.map { it.copy() }))
            }.launchIn(viewModelScope)
    }

    fun startPurchase(activity: Activity) {
        selectedProduct?.let {
            purchaseManager.buyBasePlan(activity, it)
        }
    }

    fun productSelected(index: Int) {
        selectedIndexFlow.tryEmit(index)
    }
}

sealed class PurchaseState {
    object Init : PurchaseState()
    object ConnectingPlayStore : PurchaseState()
    class ListProductUpdated(val data: List<IAPProduct>) : PurchaseState()
}