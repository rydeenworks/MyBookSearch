package com.rydeenworks.mybooksearch.usecase.booksearch

import android.util.Log
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.html.HtmlDownloader
import com.rydeenworks.mybooksearch.infrastructure.html.amazon.ParseAmazonHtml
import java.net.URI

class AnalyzeBookPage
{
    fun handle(url: URI): Book
    {
        var book: Book
        var count = 0
        do {
            val html = HtmlDownloader().downloadHtml(url)
            Log.d("AmazonBookPageAnalyzer", html)
            book = ParseAmazonHtml().handle(html)
            count++
        } while (!book.isValid() && count < 10)
        return book
    }
}