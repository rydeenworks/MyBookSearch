package com.rydeenworks.mybooksearch.ui.historypage

import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.ui.webview.WebViewAdapter

class HistoryPageWebView(
    private val webViewAdapter: WebViewAdapter
) : IHistoryPage {

    override fun showBookList(books: List<Book>) {
        webViewAdapter.showBookHistoryPage(books)
    }

    override fun showBookImage(books: List<Book>) {
        webViewAdapter.showBookImagePage(books)
    }
}