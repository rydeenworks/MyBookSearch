package com.rydeenworks.mybooksearch.usecase.customerservice

import android.content.Context
import android.content.Intent
import android.net.Uri

class ShowAppHistoryPage (
    private val context: Context
) {
    fun handle()
    {
        val uri = Uri.parse("https://rydeenworks.github.io/2023/01/14/book-search-in-library-history")
        val i = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(i)
    }
}