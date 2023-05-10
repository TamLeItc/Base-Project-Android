package fxc.dev.app.ui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.viewModels
import fxc.dev.app.R
import fxc.dev.app.databinding.ActivityWebviewBinding
import fxc.dev.app.ui.webview.client.BrowserWebChromeClient
import fxc.dev.app.ui.webview.client.WebChromeClientListener
import fxc.dev.base.constants.Transition
import fxc.dev.base.core.BaseActivity
import fxc.dev.common.extension.safeClickListener

/**
 * Created by Tam Le on 01/04/2022.
 */

class WebViewActivity : BaseActivity<WebViewVM, ActivityWebviewBinding>(R.layout.activity_webview) {

    override val viewModel: WebViewVM by viewModels()
    override val transition: Transition = Transition.SLIDE_UP

    override fun getVB(inflater: LayoutInflater) = ActivityWebviewBinding.inflate(inflater)

    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun initViews() {
        setupWebView()

        intent.getStringExtra(KEY_URL)?.run {
            binding.webView.loadUrl(this)
            binding.tvTitle.text = this
        }
    }

    override fun addListenerForViews() {
        binding.btnBack.safeClickListener {
            onBackTapped()
        }
    }

    override fun bindViewModel() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webChromeClientListener = object : WebChromeClientListener {
            override fun onProgressChanged(newProgress: Int) {
                if (newProgress < 100) {
                    binding.progressLoading.visibility = View.VISIBLE
                } else {
                    binding.progressLoading.visibility = View.GONE
                }
                binding.progressLoading.progress = newProgress
            }
        }

        binding.webView.run {
            webChromeClient = BrowserWebChromeClient(webChromeClientListener)
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
    }

    companion object {
        private val KEY_URL = "key_url"
        fun getIntent(activity: Activity, url: String): Intent {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }
    }

}