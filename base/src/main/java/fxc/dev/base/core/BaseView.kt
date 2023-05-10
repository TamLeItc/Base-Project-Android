package fxc.dev.base.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import fxc.dev.base.interfaces.IBaseView

/**
 *
 * Created by tamle on 08/05/2023
 *
 */

abstract class BaseView<VB : ViewBinding>(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), IBaseView<VB> {

    protected lateinit var binding: VB

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        binding = getVB(LayoutInflater.from(context))

        initViews()
        addListenerForViews()
    }


}