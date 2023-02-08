package com.rydeenworks.mybooksearch.domain.searchbook

import com.rydeenworks.mybooksearch.domain.Book
import java.net.URI

interface IBookPageAnalyzer {
    fun handle(uri: URI ): Book
}