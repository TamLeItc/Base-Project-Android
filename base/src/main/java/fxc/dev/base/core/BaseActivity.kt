package fxc.dev.base.core

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import fxc.dev.base.constants.Transition
import fxc.dev.base.extensions.applyTransitionIn
import fxc.dev.base.extensions.applyTransitionOut
import fxc.dev.base.interfaces.IBaseComponent
import fxc.dev.base.interfaces.IBaseView
import fxc.dev.common.bus.BusProvider
import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.common.extension.gone
import fxc.dev.common.utils.PrefUtils
import fxc.dev.common.widgets.dialog.loading.LoadingDialog
import fxc.dev.common.wrapper.AppContextWrapper
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.constants.BannerSize
import fxc.dev.fox_ads.interfaces.IAdsHelper
import fxc.dev.fox_ads.utils.AdsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

abstract class BaseActivity<VM : BaseVM, VB : ViewBinding>
protected constructor(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    IBaseView<VB>, IBaseComponent, IAdsHelper, CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    abstract val viewModel: VM
    abstract val transition: Transition

    protected val dispatchers: CoroutineDispatchers by inject()
    protected val adsHelper: AdsHelper by inject()
    protected val bus: BusProvider by inject()
    protected val appContextWrapper: AppContextWrapper by inject()

    protected lateinit var binding: VB

    private val progressDialog = LoadingDialog()
    private var pendingShowProgress = false
    private var enterAnimationComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getVB(layoutInflater)
        setContentView(binding.root)

        initialize(savedInstanceState)
        initViews()
        addListenerForViews()
        bindViewModel()

        bus.register(this)

        applyTransitionIn(transition)
    }

    override fun onDestroy() {
        super.onDestroy()

        bus.unregister(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(appContextWrapper.wrap(newBase!!, PrefUtils.language));
    }

    override fun loadBannerAds(parentView: FrameLayout, adSize: BannerSize) {
        if (AdsUtils.canShowAds()) {
            adsHelper.addBanner(
                activity = this,
                viewParent = parentView,
                adSize = adSize
            )
        } else {
            parentView.gone()
        }
    }

    /**
     * If anim moving activity has not finished but still shows loading dialog
     * the screen will be slightly jerky, not smooth on weak devices
     * To avoid that, the dialog loading is only shown when the Enter animation ends
     * */
    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        enterAnimationComplete = true
        if (pendingShowProgress) {
            showLoading(true)
        }
    }

    override fun showLoading(isShow: Boolean) {
        if (isShow) {
            if (!enterAnimationComplete) {
                pendingShowProgress = true
                return
            }
            pendingShowProgress = false
            progressDialog.show(supportFragmentManager)
        } else {
            pendingShowProgress = false
            progressDialog.dismiss()
        }
    }

    override fun onBackTapped() {
        onBackPressed()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        applyTransitionOut(transition)
    }
}