package fxc.dev.base.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

interface IBaseComponent<VB : ViewBinding> {
    fun getVB(inflater: LayoutInflater): VB
    fun initialize(savedInstanceState: Bundle?)
    fun initViews()
    fun addListenerForViews()
    fun bindViewModel()
    fun showLoading(isShow: Boolean)
}