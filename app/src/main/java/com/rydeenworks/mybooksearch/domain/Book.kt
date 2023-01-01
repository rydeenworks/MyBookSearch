package com.rydeenworks.mybooksearch.domain

data class Book(
    val title: String,
    val isbn: String)
{
    fun isValid(): Boolean
    {
        return title.isNotEmpty() && isbn.isNotEmpty()
    }
}