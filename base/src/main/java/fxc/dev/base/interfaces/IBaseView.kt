package fxc.dev.base.interfaces

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

interface IBaseView<VB : ViewBinding> {
    fun getVB(inflater: LayoutInflater): VB
    fun initViews()
    fun addListenerForViews()
}