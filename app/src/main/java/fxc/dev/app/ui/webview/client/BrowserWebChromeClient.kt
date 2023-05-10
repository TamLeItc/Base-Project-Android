package fxc.dev.app.ui.webview.client

import android.webkit.WebChromeClient
import android.webkit.WebView

/**
 * Created by Tam Le on 30/11/2021.
 */

class BrowserWebChromeClient
constructor(
    private val listener: WebChromeClientListener
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        listener.onProgressChanged(newProgress)
    }
}