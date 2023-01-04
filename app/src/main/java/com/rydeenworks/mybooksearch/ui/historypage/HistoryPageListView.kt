package com.rydeenworks.mybooksearch.ui.historypage

import android.app.Activity
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.ListView
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.webview.BookClickEventListener
import com.rydeenworks.mybooksearch.usecase.browser.OpenChromeBrowser

class HistoryPageListView(
    private val bookRepository: BookRepository,
    private val activity: Activity,
) : IHistoryPage, BookClickEventListener
{
    init {
        activity.setContentView(R.layout.activity_main)
        activity.title = "図書さがし"
        activity.setContentView(R.layout.book_search_history);
    }

    override fun updateView() {
        val books = bookRepository.getHistoryList()
//        when (viewMode) {
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_HISTORY -> webViewAdapter.showBookHistoryPage(books)
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_IMAGE -> webViewAdapter.showBookImagePage(books)
//        }

        var bookTitles = books.map { it.title }
        // ListViewにデータをセットする
        val list: ListView = activity.findViewById(R.id.book_list);
        list.adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_list_item_1,
            bookTitles)
    }

    override fun togglePageStyle() {
//        viewMode = when (viewMode) {
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_HISTORY -> HistoryPageWebView.ViewStyle.VIEW_STYLE_IMAGE
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_IMAGE -> HistoryPageWebView.ViewStyle.VIEW_STYLE_HISTORY
//        }
        updateView()
    }


    override fun OnLinkClick(uri: Uri) {
        val openChromeBrowser = OpenChromeBrowser(activity)
        openChromeBrowser.handle(uri)
    }
}