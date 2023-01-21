package com.rydeenworks.mybooksearch.ui.booksearchhistory

import androidx.lifecycle.ViewModel
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookSearchHistoryViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    fun getBookList() : List<Book>
    {
        return bookRepository.getHistoryList()
    }
}