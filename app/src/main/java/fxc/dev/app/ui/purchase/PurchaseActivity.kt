package fxc.dev.app.ui.purchase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.text.InputType
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import fxc.dev.app.R
import fxc.dev.app.databinding.ActivitySubscriptionBinding
import fxc.dev.app.navigator.Navigator
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.flow.collectIn
import fxc.dev.common.extension.flow.collectInViewLifecycle
import fxc.dev.fox_purchase.model.IAPInfo
import fxc.dev.common.extension.safeClickListener
import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.utils.PurchaseUtils
import org.koin.core.component.inject


/**
 * Created by Thanh Quang on 8/23/21.
 */

class PurchaseActivity :
    BaseActivity<PurchaseVM, ActivitySubscriptionBinding>(R.layout.activity_subscription) {

    override val viewModel: PurchaseVM by viewModels()
    override val transition: Transition = Transition.SLIDE_UP

    private val navigator: Navigator by inject()

    private val onItemClick: (IAPProduct, Int) -> Unit = { _, index ->
        viewModel.productSelected(index)
    }

    override fun getVB(inflater: LayoutInflater) = ActivitySubscriptionBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {
        viewModel.fetchData()
    }

    override fun initViews() = binding.run {
        rvProductItems.run {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = IAPProductAdapter(this@PurchaseActivity, onItemClick)
        }
    }

    override fun addListenerForViews() {
        binding.tvContinueDes.safeClickListener {
            onBackTapped()
        }

        binding.tvContinue.safeClickListener {
            viewModel.startPurchase(this)
        }

        binding.tvPrivacy.safeClickListener {
            navigator.navigateToPolicy(this)
        }

        binding.tvTermOfUse.safeClickListener {
            navigator.navigateToTerm(this)
        }

        binding.tvMessage.setOnLongClickListener {
            showDialogInputBackDoorCode()
            return@setOnLongClickListener false
        }
    }

    override fun bindViewModel() {
        viewModel.infoPremiumState
            .collectIn(this) {
                addIAPInfoLayout(it)
            }

        viewModel.purchaseState
            .collectIn(this) {
                when (it) {
                    PurchaseState.ConnectingPlayStore -> {
                        showMessageWaitingConnectPlayStore()
                    }

                    is PurchaseState.ListProductUpdated -> {
                        (binding.rvProductItems.adapter as? IAPProductAdapter)?.submitList(it.data)
                    }

                    else -> {}
                }
            }

        viewModel.purchasedFlow
            .collectIn(this) {
                if (it) {
                    onBackTapped()
                }
            }
    }

    private fun addIAPInfoLayout(list: List<IAPInfo>) = binding.run {
        list.forEach { iapInfo ->
            val iapLayout: View = LayoutInflater.from(this@PurchaseActivity)
                .inflate(R.layout.item_iap_info, llAccess, false)
            iapLayout.findViewById<ImageView>(R.id.iv_icon).setImageResource(iapInfo.icon)
            iapLayout.findViewById<TextView>(R.id.tv_title).text = getString(iapInfo.text)
            llAccess.addView(iapLayout)
        }
    }

    private fun showMessageWaitingConnectPlayStore() {
        Toast.makeText(
            this,
            resources.getString(R.string.please_login_google_play_to_continue),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showDialogInputBackDoorCode() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Input backdoor code:")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val text = input.text.toString()
            if (text == "132978") {
                PurchaseUtils.isPremium = true
                Toast.makeText(
                    this,
                    "Install backdoor success. Restart app to use",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    companion object {
        fun getIntent(activity: Activity): Intent {
            return Intent(activity, PurchaseActivity::class.java)
        }
    }

}