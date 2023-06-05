package fxc.dev.base.interfaces

import android.os.Bundle

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface IBaseComponent {
    fun initialize(savedInstanceState: Bundle?)
    fun bindViewModel()
    fun showLoading(isShow: Boolean)
}