package com.rydeenworks.mybooksearch.ui.historypage.listview

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.infrastructure.browser.OpenChromeBrowser

class HistoryPageListView(
    private val bookRepository: BookRepository,
    private val activity: Activity,
) : IHistoryPage, AdapterView.OnItemClickListener
{
    lateinit var historyListAdapter: HistoryListAdapter
    lateinit var historyItems: List<HistoryListItem>

    init {
        activity.setContentView(R.layout.activity_main)
        activity.title = "図書さがし"
        activity.setContentView(R.layout.book_search_history);
    }

    override fun updateView() {
        val books = bookRepository.getHistoryList()

        val listView: ListView = activity.findViewById(R.id.book_list)

        historyItems = books.map {
            HistoryListItem(
                it.title,
                "https://cover.openbd.jp/" + it.isbn + ".jpg",
                "https://www.amazon.co.jp/gp/search?ie=UTF8&tag=dynamitecruis-22&linkCode=ur2&linkId=4b1da2ab20d2fa32b9230f88ddab039e&camp=247&creative=1211&index=books&keywords=" + it.title)
        }

        historyListAdapter = HistoryListAdapter(activity, historyItems)
        listView.adapter = historyListAdapter
        listView.onItemClickListener = this
    }

    override fun togglePageStyle() {
//        viewMode = when (viewMode) {
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_HISTORY -> HistoryPageWebView.ViewStyle.VIEW_STYLE_IMAGE
//            HistoryPageWebView.ViewStyle.VIEW_STYLE_IMAGE -> HistoryPageWebView.ViewStyle.VIEW_STYLE_HISTORY
//        }
        updateView()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val historyItem = historyItems[position]
        val openChromeBrowser = OpenChromeBrowser(activity)
        val uri = Uri.parse(historyItem.amazonUrl)
        openChromeBrowser.handle(uri)
    }
}