package com.rydeenworks.mybooksearch.ui.webview

import android.util.Base64
import android.webkit.WebView
import com.rydeenworks.mybooksearch.usecase.BookLoadEventListener
import com.rydeenworks.mybooksearch.usecase.CreateBookImageWebPage
import com.rydeenworks.mybooksearch.usecase.CreateBookListWebPage
import org.json.JSONArray

class WebViewAdapter (
    private val webView: WebView,
    linkClickListner: BookLoadEventListener
        )
{
    init {
        val webViewClient =
            CalilWebViewClient()
        webViewClient.SetEventListener(linkClickListner)
        webView.setWebViewClient(webViewClient)
    }

    fun showBookHistoryPage(books: ArrayList<JSONArray>)
    {
        webView.post {
            val createBookListWebPage = CreateBookListWebPage()
            val htmlString = createBookListWebPage.handle(books)
            val encodedHtml = Base64.encodeToString(
                htmlString.toByteArray(),
                Base64.DEFAULT
            )
            webView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64")
        }

    }

    fun showBookImagePage(books: ArrayList<JSONArray>)
    {
        webView.post {
            val createBookImageWebPage = CreateBookImageWebPage()
            val htmlString = createBookImageWebPage.handle(books)
            val encodedHtml = Base64.encodeToString(
                htmlString.toByteArray(),
                Base64.DEFAULT
            )
            webView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64")
        }
    }
}