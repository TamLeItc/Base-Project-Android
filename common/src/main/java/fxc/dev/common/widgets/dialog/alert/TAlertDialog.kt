package fxc.dev.common.widgets.dialog.alert

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import fxc.dev.common.R
import fxc.dev.common.databinding.DialogCustomAlertBinding
import fxc.dev.common.extension.gone
import fxc.dev.common.extension.resourceColor
import fxc.dev.common.extension.visible

/**
 *
 * Created by TamLe on 1/30/21.
 *
 */

class TAlertDialog() : DialogFragment() {

    private lateinit var binding: DialogCustomAlertBinding

    private var title = ""
    private var message = ""
    private var gravity: Int = Gravity.BOTTOM
    private var cancelAble: Boolean = false
    private var showInput: Boolean = false
    private var leftAction: TAlertAction? = null
    private var rightAction: TAlertAction? = null

    private constructor(builder: Builder) : this() {
        title = builder.getTitle()
        message = builder.getMessage()
        gravity = builder.getGravity()
        cancelAble = builder.getCancelAble()
        showInput = builder.getShowInput()
        leftAction = builder.getLeftAction()
        rightAction = builder.getRightAction()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCustomAlertBinding.inflate(inflater, container, false)

        dialog!!.setCancelable(cancelAble)

        val window: Window? = dialog!!.window
        window?.setGravity(gravity)

        val params: WindowManager.LayoutParams? = window?.attributes
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
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
        show(frgManager, "AlertDialog")
    }

    private fun setupViews() {

        if (leftAction == null) {
            binding.btnLeft.visibility = View.GONE
        } else {
            when (leftAction!!.style) {
                TAlertActionStyle.CANCEL -> {
                    binding.btnLeft.setTextColor(requireContext().resourceColor(R.color.colorActionCancel))
                }
                TAlertActionStyle.CONFIRM -> {
                    binding.btnLeft.setTextColor(requireContext().resourceColor(R.color.colorActionConfirm))
                }
                TAlertActionStyle.DESTRUCTIVE -> {
                    binding.btnLeft.setTextColor(requireContext().resourceColor(R.color.colorActionDestructive))
                }
            }
        }

        if (rightAction == null) {
            binding.btnRight.visibility = View.GONE
        } else {
            when (rightAction!!.style) {
                TAlertActionStyle.CANCEL -> {
                    binding.btnRight.setTextColor(requireContext().resourceColor(R.color.colorActionCancel))
                }
                TAlertActionStyle.CONFIRM -> {
                    binding.btnRight.setTextColor(requireContext().resourceColor(R.color.colorActionConfirm))
                }
                TAlertActionStyle.DESTRUCTIVE -> {
                    binding.btnRight.setTextColor(requireContext().resourceColor(R.color.colorActionDestructive))
                }
            }
        }

        if (showInput) {
            binding.edtInput.visible()
        } else {
            binding.edtInput.gone()
        }

        with(binding) {
            tvTitle.text = title
            tvMessage.text = message
            btnLeft.text = leftAction?.title
            btnRight.text = rightAction?.title
        }

        binding.btnLeft.setOnClickListener {
            dismiss()
            leftAction?.onClick?.invoke(binding.edtInput.text.toString())
        }

        binding.btnRight.setOnClickListener {
            dismiss()
            rightAction?.onClick?.invoke(binding.edtInput.text.toString())
        }
    }

    class Builder(
        val context: Context
    ) {
        private var title = ""
        private var message = ""
        private var gravity: Int = Gravity.BOTTOM
        private var cancelAble: Boolean = false
        private var showInput: Boolean = false
        private var leftAction: TAlertAction? = null
        private var rightAction: TAlertAction? = null

        fun build() = TAlertDialog(this)

        fun setTitle(title: String) = apply { this.title = title }
        fun setTitle(@StringRes titleRes: Int) = apply { title = context.getString(titleRes) }
        fun setMessage(message: String) = apply { this.message = message }
        fun setMessage(@StringRes messageRes: Int) =
            apply { message = context.getString(messageRes) }

        fun setCancelAble(cancelAble: Boolean) = apply { this.cancelAble = cancelAble }
        fun setShowInput(showInput: Boolean) = apply { this.showInput = showInput }
        fun setLeftAction(leftAction: TAlertAction) = apply { this.leftAction = leftAction }
        fun setRightAction(rightAction: TAlertAction) = apply { this.rightAction = rightAction }

        fun getTitle() = title
        fun getMessage() = message
        fun getGravity() = gravity
        fun getCancelAble() = cancelAble
        fun getShowInput() = showInput
        fun getLeftAction() = leftAction
        fun getRightAction() = rightAction
    }
}