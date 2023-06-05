package fxc.dev.fox_ads.widget.exit_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import fxc.dev.common.extension.gone
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.constants.BannerSize
import fxc.dev.fox_ads.databinding.DialogExitAppBinding
import fxc.dev.fox_ads.utils.AdsUtils
import org.koin.android.ext.android.inject

/**
 *
 * Created by tamle on 04/05/2023
 *
 */

class ExitAppDialog(
) : DialogFragment() {

    var adUnitId: String = ""
    var listener: ExitDialogListener? = null

    private lateinit var binding: DialogExitAppBinding

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = if (AdsUtils.canShowAds()) {
                ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                ViewGroup.LayoutParams.WRAP_CONTENT
            }
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogExitAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        loadAds()
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
        show(frgManager, TAG)
    }


    private fun initView() {
        binding.btnExit.setOnClickListener {
            dismiss()
            listener?.onExitClicked()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun loadAds() = binding.run {
        if (!AdsUtils.canShowAds()) {
            flAdView.gone()
            progressBar.gone()
        } else {
            AdsHelper.getInstance().addBanner(
                activity = requireActivity(),
                viewParent = flAdView,
                adSize = BannerSize.RECTANGLE,
                adUnitId = adUnitId
            )
        }
    }

    companion object {
        const val TAG = "ExitAppDialog"
    }
}