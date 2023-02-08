package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.domain.searchbook.ISearchBookEventHandler
import com.rydeenworks.mybooksearch.domain.searchbook.SearchBookStatus
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.usecase.booksearch.AnalyzeBookPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class BookSearchHistoryViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    fun addBook(url: URI, searchBookEventHandler: ISearchBookEventHandler)
    {
        viewModelScope.launch (Dispatchers.IO){
            // analyze
            val book = AnalyzeBookPage().handle(url)

            if(book.isValid())
            {
                bookRepository.addBook(book.title, book.isbn)
                searchBookEventHandler.onBookSearchFinished(SearchBookStatus.SUCCESS, book)
            }else{
                searchBookEventHandler.onBookSearchFinished(SearchBookStatus.FAILED, book)
            }
        }
    }

    fun getBookList() : List<Book>
    {
        return bookRepository.getHistoryList()
    }
}