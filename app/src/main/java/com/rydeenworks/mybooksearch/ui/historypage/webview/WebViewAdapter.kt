package com.rydeenworks.mybooksearch.ui.historypage.webview

import android.util.Base64
import android.webkit.WebView
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.ui.historypage.webview.webpage.CreateBookImageWebPage
import com.rydeenworks.mybooksearch.ui.historypage.webview.webpage.CreateBookListWebPage

class WebViewAdapter (
    private val webView: WebView,
    linkClickListner: BookClickEventListener
        )
{
    init {
        val webViewClient =
            CalilWebViewClient()
        webViewClient.SetEventListener(linkClickListner)
        webView.setWebViewClient(webViewClient)
    }

    fun showBookHistoryPage(books: List<Book>)
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

    fun showBookImagePage(books: List<Book>)
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