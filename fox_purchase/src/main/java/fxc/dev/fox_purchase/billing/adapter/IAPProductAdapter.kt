package fxc.dev.fox_purchase.billing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import fxc.dev.fox_purchase.billing.model.IAPProduct
import fxc.dev.common.extension.resourceColor
import fxc.dev.fox_purchase.R
import fxc.dev.fox_purchase.billing.model.IAPProductType
import fxc.dev.fox_purchase.billing.utils.BillingManager
import fxc.dev.fox_purchase.databinding.ItemIapProductBinding
import fxc.dev.fox_purchase.extension.biggestPrice
import fxc.dev.fox_purchase.extension.namePeriodPage
import fxc.dev.fox_purchase.extension.priceFormatted
import fxc.dev.fox_purchase.extension.timePeriodPage

class IAPProductAdapter(
    private val context: Context,
    private val listener: (IAPProduct, Int) -> Unit
) : RecyclerView.Adapter<IAPProductAdapter.VH>() {

    private var iapProducts = listOf<IAPProduct>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class VH(
        val binding: ItemIapProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemIapProductBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.root.setOnClickListener { listener(this, position) }

                binding.clFreeTrial.isVisible = isFreeTrial
                binding.cslProductContent.isVisible = !isFreeTrial

                binding.rlProduct.run {
                    background = if (isItemSelected) ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bg_item_iap_select,
                        null
                    ) else ResourcesCompat.getDrawable(resources, R.drawable.bg_item_iap, null)
                }

                if (isItemSelected) {
                    binding.tvProductName.setTextColor(context.resourceColor(R.color.white))
                    binding.tvProductDes.setTextColor(context.resourceColor(R.color.white))
                    binding.tvProductPrice.setTextColor(context.resourceColor(R.color.white))
                    binding.tvFreeTrial.setTextColor(context.resourceColor(R.color.white))
                    binding.tvFreeDesc.setTextColor(context.resourceColor(R.color.white))
                } else {
                    binding.tvProductName.setTextColor(context.resourceColor(R.color.inappColorTextPrimary))
                    binding.tvProductDes.setTextColor(context.resourceColor(R.color.inappColorTextPrimary))
                    binding.tvProductPrice.setTextColor(context.resourceColor(R.color.inappColorTextPrimary))
                    binding.tvFreeTrial.setTextColor(context.resourceColor(R.color.inappColorTextPrimary))
                    binding.tvFreeDesc.setTextColor(context.resourceColor(R.color.inappColorTextPrimary))
                }

                binding.tvProductDes.run {
                    if (desc == null) {
                        visibility = View.GONE
                    } else {
                        visibility = View.VISIBLE
                        text = desc
                    }
                }

                if (productType == IAPProductType.Subscription) {
                    // Check free trial
                    productDetails?.let {
                        val subscriptionOfferDetails = BillingManager.shared.biggestSubscriptionOfferDetailsToken(it)
                        subscriptionOfferDetails?.let { offerDetails ->
                            val pricePhase = offerDetails.biggestPrice()

                            if (isFreeTrial) {
                                binding.tvFreeTrial.text = context.getString(R.string.free_trial_name, freeTrialDays.toString())
                                binding.tvFreeDesc.text = String.format(context.getString(R.string.free_trial_desc, pricePhase.priceFormatted,
                                    pricePhase.timePeriodPage(context)))
                            } else {
                                binding.tvProductName.text = pricePhase.namePeriodPage(context)
                                binding.tvProductPrice.text = pricePhase.priceFormatted
                            }
                        }
                    }
                } else {
                    binding.tvProductName.text = name
                    binding.tvProductPrice.text = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice ?: ""
                }

                binding.tvPopular.visibility = if (isFreeTrial) View.VISIBLE else View.GONE
            }
        }
    }

    fun setProductList(iapProducts: List<IAPProduct>) {
        this.iapProducts = iapProducts
    }

    private fun getItem(position: Int): IAPProduct = iapProducts.elementAt(position)

    override fun getItemCount(): Int = iapProducts.count()
}
