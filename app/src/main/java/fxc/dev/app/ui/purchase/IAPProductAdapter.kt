package fxc.dev.app.ui.purchase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import fxc.dev.app.R
import fxc.dev.app.databinding.ItemIapProductBinding
import fxc.dev.base.core.BaseListAdapter
import fxc.dev.base.core.ItemVH
import fxc.dev.common.extension.resourceColor
import fxc.dev.fox_purchase.extension.biggestPrice
import fxc.dev.fox_purchase.extension.biggestSubscriptionOfferDetailsToken
import fxc.dev.fox_purchase.model.IAPProduct
import fxc.dev.fox_purchase.model.IAPProductType

/**
 *
 * Created by tamle on 09/05/2023
 *
 */

class IAPProductAdapter(
    private val context: Context,
    data: List<IAPProduct>,
    private val listener: (IAPProduct, Int) -> Unit
) : BaseListAdapter<IAPProduct>(
    data
) {
    override fun onCreateVH(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemIapProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::ItemVH)
    }

    override fun onBindVH(holder: ItemVH, position: Int) {
        val binding = holder.binding as ItemIapProductBinding
        val item = getItem(position)

        binding.run {
            root.setOnClickListener { listener(item, position) }

            clFreeTrial.isVisible = item.isFreeTrial
            cslProductContent.isVisible = !item.isFreeTrial

            rlProduct.run {
                background = if (item.isItemSelected) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.bg_item_iap_select,
                    null
                ) else ResourcesCompat.getDrawable(resources, R.drawable.bg_item_iap, null)
            }

            val textColor = if (item.isItemSelected) {
                context.resourceColor(R.color.white)
            } else {
                context.resourceColor(R.color.textPrimary)
            }
            listOf(tvProductName, tvProductDes, tvProductPrice, tvFreeTrial, tvFreeDesc)
                .forEach {
                    it.setTextColor(textColor)
                }

            binding.tvProductDes.run {
                if (item.desc == null) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    text = item.desc
                }
            }

            if (item.productType == IAPProductType.Subscription) {
                item.productDetails?.biggestSubscriptionOfferDetailsToken()?.let { offerDetails ->
                    val pricePhase = offerDetails.biggestPrice()

                    if (item.isFreeTrial) {
                        tvFreeTrial.text = context.getString(
                            R.string.free_trial_name,
                            item.freeTrialDays.toString()
                        )
                        tvFreeDesc.text = String.format(
                            context.getString(
                                R.string.free_trial_desc, pricePhase.priceFormatted,
                                pricePhase.timePeriodPage(context)
                            )
                        )
                    } else {
                        tvProductName.text = pricePhase.namePeriodPage(context)
                        tvProductPrice.text = pricePhase.priceFormatted
                    }
                }
            } else {
                tvProductName.text = item.name
                tvProductPrice.text =
                    item.productDetails?.oneTimePurchaseOfferDetails?.formattedPrice ?: ""
            }
            tvPopular.visibility = if (item.isFreeTrial) View.VISIBLE else View.GONE
        }
    }
}