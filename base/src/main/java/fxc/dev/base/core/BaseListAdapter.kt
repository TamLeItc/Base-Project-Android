package fxc.dev.base.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.gms.ads.nativead.NativeAd
import fxc.dev.base.R
import fxc.dev.base.constants.NativeAdType
import fxc.dev.fox_ads.admob_ads.NativeAdUtils

/**
 *
 * Created by tamle on 18/04/2023
 *
 */


abstract class BaseListAdapter<Item : Any>(
    private val adType: NativeAdType = NativeAdType.NONE,
    private val onLoadMore: (() -> Unit)? = null,
    diffUtil: DiffUtil.ItemCallback<Item> = BaseItemCallback()
) : ListAdapter<Item, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        const val ITEM = 100
        const val LOAD_MORE = 200
    }

    private var enableLoadMore = false

    abstract fun onCreateVH(parent: ViewGroup, viewType: Int): ItemVH

    abstract fun onBindVH(holder: ItemVH, position: Int)

    override fun getItemViewType(position: Int): Int =
        if (position >= currentList.count()) {
            LOAD_MORE
        } else {
            when {
                getItem(position) is NativeAd -> {
                    adType.value
                }

                else -> {
                    ITEM
                }
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdsVH -> {
                val currentItem = getItem(position) as NativeAd
                NativeAdUtils.populateNativeAdView(
                    holder.view.findViewById(fxc.dev.fox_ads.R.id.nativeView),
                    currentItem
                )
            }

            is LoadMoreVH -> {
                onLoadMore?.invoke()
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
                throw Error("Unknown ad type")
            }

            LOAD_MORE -> {
                LoadMoreVH(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_load_more, parent, false)
                )
            }

            else -> {
                onCreateVH(parent, viewType)
            }
        }

    override fun getItemCount(): Int {
        return if (enableLoadMore) {
            currentList.count() + 1
        } else {
            currentList.count()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEnableLoadMore(enable: Boolean) {
        enableLoadMore = enable
        notifyDataSetChanged()
    }
}

class ItemVH(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

class AdsVH(val view: View) : RecyclerView.ViewHolder(view)

class LoadMoreVH(val view: View) : RecyclerView.ViewHolder(view)

class BaseItemCallback<Item : Any> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) =
        oldItem.toString() == newItem.toString()

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}