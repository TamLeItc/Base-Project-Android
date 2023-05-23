package fxc.dev.fox_ads.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import fxc.dev.fox_ads.AdsHelper
import fxc.dev.fox_ads.constants.BannerSize
import fxc.dev.fox_ads.databinding.DialogExitAppBinding
import fxc.dev.fox_ads.utils.FullScreenLayoutParams
import org.koin.android.ext.android.inject

/**
 *
 * Created by tamle on 04/05/2023
 *
 */

class ExitAppDialog
constructor(
    private val onClickExitApp: (() -> Unit)
) : DialogFragment() {

    private val adsHelper: AdsHelper by inject()

    private lateinit var binding: DialogExitAppBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = FullScreenLayoutParams.get()

        // creating the fullscreen dialog
        val dialog = object : Dialog(requireActivity()) {
            override fun onBackPressed() {
                dismiss()
            }
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        fullScreenDialog(dialog)

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
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

        setupViews()
        setupEventViews()
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
        show(frgManager, "ExitAppDialog")
    }

    private fun setupViews() = binding.run {
        loadAds()
    }

    private fun setupEventViews() = binding.run {
        tvExitApp.setOnClickListener {
            onClickExitApp.invoke()
            dismiss()
        }

        tvContinueUseApp.setOnClickListener {
            dismiss()
        }
    }

    private fun loadAds() = binding.run {
        adsHelper.addBanner(
            activity = requireActivity(),
            viewParent = flAdView,
            adSize = BannerSize.RECTANGLE,
        )
    }

    private fun fullScreenDialog(dialog: Dialog) {
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val decorView = dialog.window!!.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        dialog.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
    }
}