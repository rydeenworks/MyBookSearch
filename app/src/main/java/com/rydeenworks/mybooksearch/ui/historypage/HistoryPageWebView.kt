package com.rydeenworks.mybooksearch.ui.historypage

import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.webview.WebViewAdapter

class HistoryPageWebView(
    private val webViewAdapter: WebViewAdapter,
    private val bookRepository: BookRepository
) : IHistoryPage {
    internal enum class ViewStyle {
        VIEW_STYLE_HISTORY,
        VIEW_STYLE_IMAGE,
    }

    private var viewMode = ViewStyle.VIEW_STYLE_HISTORY

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
}