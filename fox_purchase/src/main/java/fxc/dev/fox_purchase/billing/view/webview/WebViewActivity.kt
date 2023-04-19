package fxc.dev.fox_purchase.billing.view.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import fxc.dev.fox_purchase.billing.view.webview.listener.WebChromeClientListener
import fxc.dev.fox_purchase.databinding.ActivityWebviewBinding

/**
 * Created by Tam Le on 01/04/2022.
 */

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupEventViews()
    }

    private fun setupViews() {
        setupWebView()

        intent.getStringExtra(KEY_URL)?.run {
            binding.webView.loadUrl(this)
            binding.tvTitle.text = this
        }
    }

    private fun setupEventViews() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
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