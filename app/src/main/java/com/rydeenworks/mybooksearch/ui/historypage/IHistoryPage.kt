package com.rydeenworks.mybooksearch.ui.historypage

import com.rydeenworks.mybooksearch.domain.Book

interface IHistoryPage {
    fun showBookList(books: List<Book>)
    fun showBookImage(books: List<Book>)
}