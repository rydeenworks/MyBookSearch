package com.rydeenworks.mybooksearch.usecase.booksearch

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.rydeenworks.mybooksearch.domain.Book

class RequestBookSearch {
    fun handle (book: Book, activity: Activity){
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val uri = Uri.parse("https://calil.jp/book/" + book.isbn)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(intent)
        }
    }
}