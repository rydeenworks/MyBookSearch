package com.rydeenworks.mybooksearch.usecase.html

import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.html.HtmlDownloadEventListner
import com.rydeenworks.mybooksearch.infrastructure.html.HtmlDownloader

class DownloadAmazonHtmlService(
    private val amazonHtmlEventListner: AmazonHtmlEventListner
) : HtmlDownloadEventListner {

    lateinit var book: Book

    fun download(url: String)
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
        amazonHtmlEventListner.OnFailedDownload()
    }

    override fun OnSuccessDownload() {
        amazonHtmlEventListner.OnSuccessDownload(book)
    }
}