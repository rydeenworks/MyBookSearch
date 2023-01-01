package com.rydeenworks.mybooksearch.infrastructure

import android.content.SharedPreferences

class BookRepository (
    private val historyLastIndexKeyStr: String,
    private val sharedPref: SharedPreferences
){

    fun getBookNum() : Int
    {
        return getLastIndex()
    }

    // 0 <= lastIndex < HISTORY_MAX_NUM -1
    // プリファレンスがない場合は -1
    private fun getLastIndex(): Int {
        var lastIndex = -1
        lastIndex = sharedPref.getInt(historyLastIndexKeyStr, lastIndex)
        return lastIndex
    }

}