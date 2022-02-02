package com.livo.nuo.view.webView

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jaeger.library.StatusBarUtil
import com.livo.nuo.R
import com.livo.nuo.utility.AppUtils
import com.livo.nuo.utility.LocalizeActivity


class TransportApplication : LocalizeActivity() {

    lateinit var webView:WebView
    private var currActivity : Activity = this

    private lateinit var dialog: Dialog

    var url=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_application)

        webView=findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        url=intent.getStringExtra("url")!!

        webView.loadUrl(url)


        /*webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, urlNewString: String): Boolean {
               showProgressBar()
                return true
            }

            override fun onPageStarted(view: WebView, url: String, facIcon: Bitmap) {
                showProgressBar()
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
               hideProgressBar()
            }
        })*/

    }

    fun showProgressBar(){
        dialog =  AppUtils.showProgress(this)
    }

    fun hideProgressBar(){
        AppUtils.hideProgress(dialog)
    }

    protected override fun setStatusBar() {

        val mColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setLightMode(currActivity)
    }
}