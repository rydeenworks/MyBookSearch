package com.rydeenworks.mybooksearch.ui.historypage.listview

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.browser.OpenChromeBrowser

class HistoryListAdapter(
    context: Context,
    private val historyItems: List<HistoryListItem>
): ArrayAdapter<HistoryListItem>(context, 0, historyItems)
{
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup): View {
        val historyItem = historyItems[position]

        // レイアウトの設定
        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.book_list_item, parent, false)
        }

        // 各Viewの設定
        val imageView = view?.findViewById<ImageView>(R.id.book_image)
        if(imageView != null)
        {
            val setBookImageView = SetBookImageView(imageView)
            setBookImageView.handle(historyItem.imageUrl)
        }

        val name = view?.findViewById<TextView>(R.id.book_title)
        name?.text = historyItem.title

//        val age = view?.findViewById<TextView>(R.id.book_detail)
//        age?.text = historyItem.amazonUrl

        return view!!
    }
}