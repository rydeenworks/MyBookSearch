package com.rydeenworks.mybooksearch.usecase.booksearch

import android.app.Activity
import android.net.Uri
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.infrastructure.browser.OpenChromeBrowser

class SearchBookInAmazon(
    private val activity: Activity,
    private val bookRepository: BookRepository,
) {
    fun handle(book: Book)
    {
        val openChromeBrowser = OpenChromeBrowser(activity)
        bookRepository.getBookNum()
        val url = "https://www.amazon.co.jp/gp/search?ie=UTF8&tag=dynamitecruis-22&linkCode=ur2&linkId=4b1da2ab20d2fa32b9230f88ddab039e&camp=247&creative=1211&index=books&keywords=" + book.title
        val uri = Uri.parse(url)
        openChromeBrowser.handle(uri)
    }
}