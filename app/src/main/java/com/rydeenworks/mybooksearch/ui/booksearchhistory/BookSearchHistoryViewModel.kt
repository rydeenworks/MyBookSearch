package com.rydeenworks.mybooksearch.ui.booksearchhistory

import androidx.lifecycle.ViewModel
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookSearchHistoryViewModel @Inject constructor(
//    private val bookRepository: BookRepository,
) : ViewModel() {
    // TODO: Implement the ViewModel
    @Inject
    lateinit var bookRepository: BookRepository
//    fun getBookList() : List<Book>
//    {
//        return bookRepository.getHistoryList()
//    }


    fun getFluits() :List<String>
    {
        val fruits = listOf("Apple", "Orange", "Grape", "Peach", "Strawberry")
        return fruits
    }
}