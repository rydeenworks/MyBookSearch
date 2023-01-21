package com.rydeenworks.mybooksearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.ui.booksearchhistory.BookSearchHistoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSearchHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search_history)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BookSearchHistoryFragment.newInstance())
                .commitNow()
        }
    }
}