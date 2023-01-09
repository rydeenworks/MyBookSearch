package com.rydeenworks.mybooksearch.ui.historypage.webview

import android.app.Activity
import android.net.Uri
import android.webkit.WebView
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.infrastructure.browser.OpenChromeBrowser

class HistoryPageWebView(
    private val bookRepository: BookRepository,
    private val activity: Activity,
) : IHistoryPage, BookClickEventListener
{
    internal enum class ViewStyle {
        VIEW_STYLE_HISTORY,
        VIEW_STYLE_IMAGE,
    }

    private val webViewAdapter: WebViewAdapter
    private var viewMode = ViewStyle.VIEW_STYLE_HISTORY

    init {
        activity.setContentView(R.layout.activity_main)
        activity.setTitle("図書さがし")

        val calilWebView: WebView = activity.findViewById(R.id.webView_calil)
        webViewAdapter = WebViewAdapter(calilWebView, this)
    }

    override fun updateView() {
        val books = bookRepository.getHistoryList()
        when (viewMode) {
            ViewStyle.VIEW_STYLE_HISTORY -> webViewAdapter.showBookHistoryPage(books)
            ViewStyle.VIEW_STYLE_IMAGE -> webViewAdapter.showBookImagePage(books)
        }
    }

    override fun togglePageStyle() {
        viewMode = when (viewMode) {
            ViewStyle.VIEW_STYLE_HISTORY -> ViewStyle.VIEW_STYLE_IMAGE
            ViewStyle.VIEW_STYLE_IMAGE -> ViewStyle.VIEW_STYLE_HISTORY
        }
        updateView()
    }

    override fun OnLinkClick(uri: Uri) {
        val openChromeBrowser = OpenChromeBrowser(activity)
        openChromeBrowser.handle(uri)
    }
}