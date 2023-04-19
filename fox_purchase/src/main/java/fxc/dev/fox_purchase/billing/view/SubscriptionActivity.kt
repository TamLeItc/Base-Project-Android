package fxc.dev.fox_purchase.billing.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fxc.dev.fox_purchase.billing.adapter.IAPProductAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.text.InputType

import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import fxc.dev.common.utils.PrefUtils
import fxc.dev.fox_purchase.R
import fxc.dev.fox_purchase.billing.model.IAPInfo
import fxc.dev.fox_purchase.billing.model.IAPProduct
import fxc.dev.fox_purchase.billing.utils.BillingManager

import fxc.dev.fox_purchase.billing.view.webview.WebViewActivity
import fxc.dev.fox_purchase.databinding.ActivitySubscriptionBinding


/**
 * Created by Thanh Quang on 8/23/21.
 */
class SubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding

    private val billingClient = BillingManager.shared

    private val productAdapter by lazy(LazyThreadSafetyMode.NONE) {
        IAPProductAdapter(
            this
        ) { _, index ->
            selectedIndexS.tryEmit(index)
        }
    }

    private val iapInfos: List<IAPInfo> = listOf(
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_1),
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_2),
        IAPInfo(R.drawable.ic_arrow_right, R.string.info_inapp_3)
    )

    private var selectedProduct: IAPProduct? = null
    private val selectedIndexS = MutableStateFlow(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!billingClient.serverIsConnected) {
            Toast.makeText(
                this,
                resources.getString(R.string.please_login_google_play_to_continue),
                Toast.LENGTH_LONG
            ).show()
        }

        initView()
        listenerViewModel()
    }

    private fun initView() = binding.run {

        tvContinueDes.setOnClickListener { finish() }

        tvContinue.setOnClickListener { selectedProduct?.let { startPurchase(it) } }

        tvPrivacy.setOnClickListener {
            intent?.getStringExtra(PRIVACY_POLICY_KEY)?.run {
                val intent = WebViewActivity.getIntent(this@SubscriptionActivity, this)
                startActivity(intent)
            }
        }

        tvTermOfUse.setOnClickListener {
            intent?.getStringExtra(TERM_OF_USE_KEY)?.run {
                val intent = WebViewActivity.getIntent(this@SubscriptionActivity, this)
                startActivity(intent)
            }
        }

        tvMessage.setOnLongClickListener {
            showDialogInputBackDoorCode()
            return@setOnLongClickListener false
        }

        iapInfos.forEach {
            addIAPInfoLayout(it)
        }

        rvProductItems.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = productAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun listenerViewModel() {
        selectedIndexS.combine(billingClient.iapProductListS) { selectedIndex, items ->
            items.forEachIndexed { index, iapProduct ->
                iapProduct.isItemSelected = selectedIndex == index
            }
            items
        }
            .onEach { list ->
                selectedProduct = list.firstOrNull { it.isItemSelected }
            }
            .onEach { productAdapter.setProductList(it) }
            .launchIn(lifecycleScope)

        billingClient.billingPurchasedS
            .onEach { finish() }
            .launchIn(lifecycleScope)
    }

    private fun addIAPInfoLayout(iapInfo: IAPInfo) = binding.run {
        val iapLayout: View = LayoutInflater.from(this@SubscriptionActivity)
            .inflate(R.layout.item_iap_info, llAccess, false)
        iapLayout.findViewById<ImageView>(R.id.iv_icon).setImageResource(iapInfo.icon)
        iapLayout.findViewById<TextView>(R.id.tv_title).text = getString(iapInfo.text)
        llAccess.addView(iapLayout)
    }

    private fun startPurchase(item: IAPProduct) {
        item.productDetails?.let {
            billingClient.buyBasePlan(this, it)
        }
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
                PrefUtils.isBackDoorUsed = true
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
        const val PRIVACY_POLICY_KEY = "PRIVACY_POLICY_KEY"
        const val TERM_OF_USE_KEY = "TERM_OF_USE_KEY"
        const val IS_OPEN_APP_KEY = "TERM_OF_USE_KEY"
    }

}