package fxc.dev.fox_purchase.billing.view.webview

import android.webkit.WebChromeClient
import android.webkit.WebView
import fxc.dev.fox_purchase.billing.view.webview.listener.WebChromeClientListener

/**
 * Created by Tam Le on 30/11/2021.
 */

class BrowserWebChromeClient
constructor(
    val listener: WebChromeClientListener
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        listener.onProgressChanged(newProgress)
    }
}