package fxc.dev.base.core

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import fxc.dev.fox_ads.constants.BannerSize

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface BaseComponent<VB : ViewBinding> {
    fun getVB(inflater: LayoutInflater): VB
    fun initialize(savedInstanceState: Bundle?)
    fun initViews()
    fun addListenerForViews()
    fun bindViewModel()
    fun loadBannerAds(parentView: FrameLayout, adSize: BannerSize = BannerSize.SMART)
    fun showLoading(isShow: Boolean)
}