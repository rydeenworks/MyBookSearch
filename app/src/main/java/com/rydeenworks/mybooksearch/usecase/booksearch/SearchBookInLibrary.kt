package com.rydeenworks.mybooksearch.usecase.booksearch

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.SearchBookActivity

class SearchBookInLibrary(
    private val activity: Activity,
    private val bookRepository: BookRepository
): AnalyzeBookPageEventListner {
    private val analyzeBookPage = AnalyzeBookPage(this)

    fun handle(): Boolean
    {
        val bookPageUrl = getBookPageUrl()
        if(bookPageUrl.isNullOrEmpty())
        {
            return false
        }

        val toast: Toast = Toast.makeText(activity, "本を検索中・・・", Toast.LENGTH_LONG)
        toast.show()

        analyzeBookPage.handle(bookPageUrl)
        return true
    }

    override fun OnFailedAnalyzingBookPage() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val toast: Toast = Toast.makeText(activity, "このページは検索できませんでした", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun OnSuccessAnalyzingBookPage(book: Book) {
//        // request: amazon/calil/mercali/rakuten/yahoo/
//        val intent = Intent(activity, SearchBookActivity::class.java)
//        activity.startActivity(intent)


        val requestBookSearch = RequestBookSearch()
        requestBookSearch.handle(book, activity)
        bookRepository.addBook(book.title, book.isbn)
    }

    private fun getBookPageUrl(): String? {
        val intent: Intent = activity.intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND != action) {
            return null
        }
        if (type == null) {
            return null
        }
        if ("text/plain" != type) {
            return null
        }

        return intent.getStringExtra(Intent.EXTRA_TEXT)
    }
}