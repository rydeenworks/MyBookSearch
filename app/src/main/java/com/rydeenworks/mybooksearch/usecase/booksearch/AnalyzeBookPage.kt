package com.rydeenworks.mybooksearch.usecase.booksearch

import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.html.HtmlDownloadEventListner
import com.rydeenworks.mybooksearch.infrastructure.html.HtmlDownloader
import com.rydeenworks.mybooksearch.infrastructure.html.amazon.ParseAmazonHtml

class AnalyzeBookPage(
    private val analyzeBookPageEventListner: AnalyzeBookPageEventListner
) : HtmlDownloadEventListner {

    lateinit var book: Book

    fun handle(url: String)
    {
        HtmlDownloader(this).execute(url)
    }

    override fun OnDownloaded(html: String):Boolean {
        val amazonParser = ParseAmazonHtml()
        book = amazonParser.handle(html)
        if (!book.isValid()) {
            return false
        }
        return true
    }

    override fun OnFailedDownload() {
        analyzeBookPageEventListner.OnFailedAnalyzingBookPage()
    }

    override fun OnSuccessDownload() {
        analyzeBookPageEventListner.OnSuccessAnalyzingBookPage(book)
    }
}