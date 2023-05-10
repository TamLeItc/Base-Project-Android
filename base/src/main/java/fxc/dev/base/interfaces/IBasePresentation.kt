package fxc.dev.base.interfaces

import android.os.Bundle
import android.widget.FrameLayout
import fxc.dev.fox_ads.constants.BannerSize

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface IBasePresentation {
    fun initialize(savedInstanceState: Bundle?)
    fun bindViewModel()
    fun showLoading(isShow: Boolean)
    fun onBackTapped()
}