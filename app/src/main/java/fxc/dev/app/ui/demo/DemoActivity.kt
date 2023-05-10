package fxc.dev.app.ui.demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import fxc.dev.app.R
import fxc.dev.app.databinding.ActivityDemoBinding
import fxc.dev.app.databinding.ActivityMainBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.gone
import fxc.dev.common.extension.safeClickListener
import fxc.dev.fox_ads.constants.BannerSize
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.supervisorScope
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

class DemoActivity : BaseActivity<DemoVM, ActivityDemoBinding>(R.layout.activity_demo) {

    override val viewModel: DemoVM by viewModels()
    override val transition: Transition = Transition.SLIDE_LEFT

    private val navigator: Navigator by inject()

    override fun getVB(inflater: LayoutInflater) = ActivityDemoBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun initViews() {

        loadBannerAds(parentView = binding.flAdView, adSize = BannerSize.SMART)
    }

    override fun addListenerForViews() = binding.run {
        btInapp.safeClickListener {
            navigator.navigateToInApp(this@DemoActivity)
        }
    }

    override fun bindViewModel() {
        viewModel.purchasedFlow
            .onEach {
                if (it) {
                    binding.flAdView.gone()
                }
            }.launchIn(lifecycleScope)
    }

}