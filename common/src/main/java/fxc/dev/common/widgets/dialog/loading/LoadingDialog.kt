package fxc.dev.common.widgets.dialog.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import fxc.dev.common.databinding.DialogCustomAlertBinding
import fxc.dev.common.databinding.DialogLoadingBinding

/**
 *
 * Created by tamle on 18/04/2023
 *
 */

class LoadingDialog: DialogFragment() {

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLoadingBinding.inflate(inflater, container, false)

        dialog!!.setCancelable(false)

        val window: Window? = dialog!!.window
        window?.setGravity(Gravity.CENTER)

        val params: WindowManager.LayoutParams? = window?.attributes
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val mDismissedField = DialogFragment::class.java.getDeclaredField("mDismissed")
        mDismissedField.isAccessible = true
        mDismissedField.setBoolean(this, false)

        val mShownByMeField = DialogFragment::class.java.getDeclaredField("mShownByMe")
        mShownByMeField.isAccessible = true
        mShownByMeField.setBoolean(this, true)

        manager.beginTransaction()
            .add(this, tag)
            .commitAllowingStateLoss()
    }

    fun show(frgManager: FragmentManager) {
        show(frgManager, "LoadingDialog")
    }
}