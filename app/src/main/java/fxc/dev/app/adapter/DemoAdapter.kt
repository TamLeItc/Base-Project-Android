package fxc.dev.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import fxc.dev.app.R
import fxc.dev.app.databinding.ItemUserBinding
import fxc.dev.base.constants.NativeAdType
import fxc.dev.base.core.BaseListAdapter
import fxc.dev.base.core.ItemVH
import fxc.dev.common.extension.safeClickListener
import fxc.dev.core.domain.model.User

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

class DemoAdapter(
    private val context: Context,
    private val onClickItem: (User) -> Unit,
    onLoadMore: (() -> Unit)? = null,
) : BaseListAdapter<Any>(adType = NativeAdType.SMALL, onLoadMore = onLoadMore) {

    override fun onCreateVH(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let(::ItemVH)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindVH(holder: ItemVH, position: Int) {
        val binding = holder.binding as ItemUserBinding
        val user = getItem(position) as User

        binding.tvFullName.text = user.getFullName()
        binding.tvEmail.text = user.email

        Glide.with(context)
            .load(user.avatar)
            .placeholder(R.drawable.img_avatar)
            .centerCrop()
            .into(binding.ivAvatar)

        holder.itemView.safeClickListener {
            onClickItem.invoke(user)
        }
    }

}