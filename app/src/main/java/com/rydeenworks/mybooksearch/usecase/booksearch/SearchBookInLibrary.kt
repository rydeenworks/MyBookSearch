package com.rydeenworks.mybooksearch.usecase.booksearch

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.usecase.html.AmazonHtmlEventListner
import com.rydeenworks.mybooksearch.usecase.html.DownloadAmazonHtmlService

class SearchBookInLibrary(
    private val activity: Activity,
    private val bookRepository: BookRepository
): AmazonHtmlEventListner {
    private val downloadAmazonHtmlService = DownloadAmazonHtmlService(this)


    fun handle(): Boolean
    {
        val intent: Intent = activity.getIntent()
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND != action) {
            return false
        }
        if (type == null) {
            return false
        }
        if ("text/plain" != type) {
            return false
        }

        val bookPageUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
        if(bookPageUrl.isNullOrEmpty())
        {
            return false
        }

        val toast: Toast = Toast.makeText(activity, "本を検索中・・・", Toast.LENGTH_LONG)
        toast.show()

        downloadAmazonHtmlService.download(bookPageUrl)
        return true
    }

    override fun OnFailedDownload() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val toast: Toast = Toast.makeText(activity, "このページは検索できませんでした", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun OnSuccessDownload(book: Book) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val uri = Uri.parse("https://calil.jp/book/" + book.isbn)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
            bookRepository.addBook(book.title, book.isbn)
        }
    }
}