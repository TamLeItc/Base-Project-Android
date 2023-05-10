package fxc.dev.base.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.gms.ads.nativead.NativeAd
import fxc.dev.base.constants.NativeAdType
import fxc.dev.fox_ads.admob_ads.NativeAdUtils

/**
 *
 * Created by tamle on 18/04/2023
 *
 */


abstract class BaseListAdapter<Item : Any>(
    data: List<Item>,
    private val adType: NativeAdType = NativeAdType.NONE
) : ListAdapter<Item, RecyclerView.ViewHolder>(BaseItemCallback<Item>()) {

    companion object {
        const val ITEM = 100
    }

    var data: MutableList<Item> = mutableListOf()

    init {
        this.data.addAll(data)
    }

    abstract fun onCreateVH(parent: ViewGroup, viewType: Int): ItemVH

    abstract fun onBindVH(holder: ItemVH, position: Int)

    override fun getItemViewType(position: Int): Int =
        when {
            getItem(position) is NativeAd -> {
                adType.value
            }

            else -> {
                ITEM
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BaseListAdapter<*>.AdsVH -> {
                val currentItem = getItem(position) as NativeAd
                NativeAdUtils.populateNativeAdView(
                    holder.view.findViewById(fxc.dev.fox_ads.R.id.nativeView),
                    currentItem
                )
            }

            else -> {
                onBindVH((holder as ItemVH), position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            NativeAdType.SMALL.value -> {
                AdsVH(
                    LayoutInflater.from(parent.context)
                        .inflate(fxc.dev.fox_ads.R.layout.item_ad_small, parent, false)
                )
            }

            NativeAdType.MEDIUM.value -> {
                AdsVH(
                    LayoutInflater.from(parent.context)
                        .inflate(fxc.dev.fox_ads.R.layout.item_ad_medium, parent, false)
                )
            }

            NativeAdType.NONE.value -> {
                throw Error("Unknown type")
            }

            else -> {
                onCreateVH(parent, viewType)
            }
        }

    override fun getItemCount() = data.size

    override fun getItem(position: Int): Item {
        return data[position]
    }

    fun resetData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun updateData(list: List<Item>) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    fun appendData(list: List<Item>) {
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, item: Item) {
        data[position] = item
        notifyItemChanged(position)
    }

    inner class AdsVH(val view: View) : RecyclerView.ViewHolder(view)

}

class ItemVH(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

class AdsVH(val view: View) : RecyclerView.ViewHolder(view)

class BaseItemCallback<Item : Any> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) =
        oldItem.toString() == newItem.toString()

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}