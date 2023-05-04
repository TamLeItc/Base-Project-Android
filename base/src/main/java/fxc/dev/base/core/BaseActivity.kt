package fxc.dev.base.core

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.markodevcic.peko.ActivityRotatingException
import fxc.dev.base.constants.Transaction
import fxc.dev.base.extensions.applyTransitionIn
import fxc.dev.base.extensions.applyTransitionOut
import fxc.dev.common.bus.BusProvider
import fxc.dev.common.utils.PrefUtils
import fxc.dev.common.widgets.dialog.loading.LoadingDialog
import fxc.dev.common.wrapper.AppContextWrapper
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.constants.BannerSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    BaseComponent<VB>, CoroutineScope, KoinComponent {

    val mainJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + mainJob

    abstract val viewModel: VM
    abstract val transaction: Transaction

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

        applyTransitionIn(transaction)

        initialize(savedInstanceState)
        initViews()
        addListenerForViews()
        bindViewModel()

        bus.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        bus.unregister(this)
        if (isChangingConfigurations) {
            mainJob.completeExceptionally(ActivityRotatingException())
        } else {
            mainJob.cancel()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(appContextWrapper.wrap(newBase!!, PrefUtils.language));
    }

    override fun onBackPressed() {
        applyTransitionOut(transaction)
        onBackPressedDispatcher.onBackPressed()
    }

    override fun loadBannerAds(parentView: FrameLayout, adSize: BannerSize) {
        adsHelper.addBanner(
            activity = this,
            viewParent = parentView,
            adSize = adSize
        )
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
}