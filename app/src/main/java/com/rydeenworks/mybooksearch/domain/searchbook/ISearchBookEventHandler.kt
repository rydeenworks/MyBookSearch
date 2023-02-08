package com.rydeenworks.mybooksearch.domain.searchbook

import com.rydeenworks.mybooksearch.domain.Book

enum class SearchBookStatus{
    SUCCESS,
    FAILED,
}

interface ISearchBookEventHandler {
    fun onBookSearchFinished(status: SearchBookStatus, book: Book)
}