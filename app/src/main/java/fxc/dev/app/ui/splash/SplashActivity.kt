package fxc.dev.app.ui.splash

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import fxc.dev.app.R
import fxc.dev.app.databinding.ActivitySplashBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.base.core.BaseActivity
import fxc.dev.base.constants.Transaction
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.inject

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class SplashActivity : BaseActivity<SplashVM, ActivitySplashBinding>(R.layout.activity_splash) {

    override val viewModel: SplashVM by viewModels()
    override val transaction: Transaction
        get() = Transaction.NONE

    private val navigator: Navigator by inject()

    override fun getVB(inflater: LayoutInflater) = ActivitySplashBinding.inflate(layoutInflater)

    override fun initialize(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        viewModel.fetchData()
    }

    override fun initViews() {

    }

    override fun addListenerForViews() {

    }

    override fun bindViewModel() {
        viewModel.launchAppState
            .onEach {
                when (it) {
                    LauncherState.Init -> {
                    }
                    LauncherState.RequireUpdateSDK -> {
                        showDialogUpdateVersion()
                    }
                    LauncherState.GoToMain -> {
                        navigator.navigateToMain(this)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun showDialogUpdateVersion() {
        showExitDialog(
            this,
            getString(R.string.new_version_available),
            getString(R.string.minimum_android_sdk_require_message),
            getString(R.string.ok)
        )
    }

    private fun showExitDialog(
        activity: Activity,
        str: String?,
        str2: String?,
        str3: String?
    ): AlertDialog? {
        return AlertDialog.Builder(activity).setTitle(str).setMessage(str2)
            .setIcon(R.mipmap.ic_launcher).setCancelable(false).setPositiveButton(
                str3
            ) { _: DialogInterface?, _: Int -> activity.finish() }
            .show()
    }
}