package fxc.dev.app.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import fxc.dev.app.R
import fxc.dev.app.databinding.ActivityMainBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.app.utils.AppUtils
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.flow.collectIn
import fxc.dev.common.extension.resourceColor
import fxc.dev.common.extension.safeClickListener
import fxc.dev.common.widgets.dialog.alert.TAlertAction
import fxc.dev.common.widgets.dialog.alert.TAlertActionStyle
import fxc.dev.common.widgets.dialog.alert.TAlertDialog
import fxc.dev.core.domain.model.AppConfig
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.widget.exit_dialog.ExitAppDialog
import fxc.dev.fox_ads.widget.exit_dialog.ExitDialogListener
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 14/04/2023
 *
 */

class MainActivity : BaseActivity<MainVM, ActivityMainBinding>(R.layout.activity_main) {
    override val viewModel: MainVM by viewModels()
    override val transition: Transition
        get() = Transition.NONE

    private val navigator: Navigator by inject()

    private val exitDialog = ExitAppDialog()

    private val exitDialogListener = object: ExitDialogListener {
        override fun onExitClicked() {
            finishAffinity()
        }
    }

    override fun getVB(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            (supportFragmentManager.findFragmentByTag(ExitAppDialog.TAG) as? ExitAppDialog)?.listener = exitDialogListener
        }
    }

    override fun initViews() = binding.run {
        binding.btDemo.setTextColor(resourceColor(R.color.accent))
    }

    override fun addListenerForViews() = binding.run {
        btDemo.safeClickListener {
            navigator.navigateToDemoRoom(this@MainActivity)
        }
    }

    override fun bindViewModel() {
        viewModel.appConfigState
            .collectIn(this, Lifecycle.State.CREATED) {
                when (it) {
                    AppConfigState.Init -> {}

                    AppConfigState.Start -> {
                        showLoading(true)
                    }

                    is AppConfigState.Success -> {
                        showLoading(false)
                        if (it.appConfig.isSuspend) {
                            showDialogAppSuspend(it.appConfig)
                        } else {
                            showDialogRequireUpdate(it.appConfig.requireUpdate)
                        }
                    }

                    AppConfigState.Failure -> {
                        showLoading(false)
                    }
                }
            }
    }

    override fun onBackTapped() {
        AdsHelper.getInstance().showInterstitialAd(this@MainActivity) {
            exitDialog.adUnitId = getString(R.string.ads_banner_large_id)
            exitDialog.listener = exitDialogListener
            exitDialog.show(supportFragmentManager)
        }
    }

    private fun showDialogRequireUpdate(isRequire: Boolean) {
        val builder = TAlertDialog.Builder(this)
            .setCancelAble(!isRequire)
            .setTitle(R.string.new_version_title)
            .setMessage(R.string.new_version_message)
            .setRightAction(TAlertAction(getString(R.string.ok), TAlertActionStyle.CONFIRM) {
                AppUtils.gotoAppInGooglePlay(this)
            })

        if (!isRequire) {
            builder.setLeftAction(
                TAlertAction(
                    getString(R.string.cancel),
                    TAlertActionStyle.CANCEL
                )
            )
        }

        builder.build().show(supportFragmentManager)
    }

    private fun showDialogAppSuspend(appConfig: AppConfig) {
        TAlertDialog.Builder(this)
            .setCancelAble(false)
            .setTitle(R.string.app_suspend_title)
            .setMessage(R.string.app_suspend_message)
            .setRightAction(TAlertAction(getString(R.string.ok), TAlertActionStyle.CONFIRM) {
                this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(appConfig.newApp)))
            }).build().show(supportFragmentManager)
    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, MainActivity::class.java)
        }
    }
}