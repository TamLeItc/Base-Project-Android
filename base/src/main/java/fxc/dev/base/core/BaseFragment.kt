package fxc.dev.base.core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.markodevcic.peko.ActivityRotatingException
import fxc.dev.base.interfaces.IBaseComponent
import fxc.dev.base.interfaces.IBaseView
import fxc.dev.common.bus.BusProvider
import fxc.dev.common.dispatcher.CoroutineDispatchers
import fxc.dev.common.extension.gone
import fxc.dev.common.utils.PrefUtils
import fxc.dev.common.wrapper.AppContextWrapper
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.constants.BannerSize
import fxc.dev.fox_ads.interfaces.IAdsHelper
import fxc.dev.fox_ads.utils.AdsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

abstract class BaseFragment<VM : BaseVM, VB : ViewBinding>
protected constructor() : Fragment(),
    IAdsHelper, IBaseComponent<VB>, CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    abstract val viewModel: VM

    protected val dispatchers: CoroutineDispatchers by inject()
    protected val bus: BusProvider by inject()
    protected val appContextWrapper: AppContextWrapper by inject()

    protected lateinit var binding: VB

    override fun onAttach(context: Context) {
        super.onAttach(appContextWrapper.wrap(context, PrefUtils.language));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = getVB(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(savedInstanceState)
        initViews()
        addListenerForViews()
        bindViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
    }

    override fun showLoading(isShow: Boolean) {
        (activity as? BaseActivity<*, *>)?.run {
            showLoading(isShow)
        }
    }

    override fun loadBannerAds(viewParent: FrameLayout, adUnitId: String) {
        if (AdsUtils.canShowAds()) {
            AdsHelper.getInstance().addBanner(
                activity = requireActivity(),
                viewParent = viewParent,
                adSize = BannerSize.SMART,
                adUnitId = adUnitId
            )
        } else {
            viewParent.gone()
        }
    }
}