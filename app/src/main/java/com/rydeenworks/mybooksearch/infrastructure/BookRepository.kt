package com.rydeenworks.mybooksearch.infrastructure

import android.content.SharedPreferences
import com.rydeenworks.mybooksearch.domain.Book
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner
import org.json.JSONArray

class BookRepository (
    private val historyLastIndexKeyStr: String,
    private val sharedPrefBooks: SharedPreferences,
    private val bookRepositoryEventListner: BookRepositoryEventListner
){
    private val HISTORY_MAX_NUM = 100

    fun addBook(bookTitle: String, isbn: String)
    {
        //登録済みの履歴は追加しない
        if (isExistHistoryBookTitle(bookTitle)) {
            return
        }

        var lastIndex = getLastIndex()
        lastIndex = incrementLastIndex(lastIndex)
        saveHistory(lastIndex, bookTitle, isbn)
        saveLastIndex(lastIndex)

        bookRepositoryEventListner.onUpdateBookRepository()
    }
    fun getBookNum() : Int
    {
        return getLastIndex()
    }

    private fun incrementLastIndex(lastIndex: Int): Int {
        var lastIndex = lastIndex
        lastIndex++
        if (lastIndex == HISTORY_MAX_NUM) {
            lastIndex = 0
        }
        return lastIndex
    }

    private fun decrementLastIndex(lastIndex: Int): Int {
        var lastIndex = lastIndex
        lastIndex--
        if (lastIndex == -1) {
            lastIndex = HISTORY_MAX_NUM - 1
        }
        return lastIndex
    }


    fun getHistoryText(): java.util.ArrayList<JSONArray> {
        val historyList = java.util.ArrayList<JSONArray>()
        val lastIndex = getLastIndex()
        var currIndex = lastIndex
        do {
            val strJson: String =
                sharedPrefBooks.getString(Integer.toString(currIndex), "")
                    ?: break
            if (strJson.isEmpty()) {
                break
            }
            try {
                val jsonAry = JSONArray(strJson)
                historyList.add(jsonAry)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                break
            }
            currIndex = decrementLastIndex(currIndex)
        } while (currIndex != lastIndex)
        return historyList
    }

    fun getHistoryList(): List<Book> {
        val historyList = getHistoryText()
        val books = historyList.map { Book(it.get(0).toString(), it.get(1).toString()) }
        return books
    }


    fun isExistHistoryBookTitle(bookTitle: String): Boolean {
        val books = getHistoryList()
        for (book in books) {
            if(book.title == bookTitle)
            {
                return true
            }
        }
        return false
    }

    // 0 <= lastIndex < HISTORY_MAX_NUM -1
    // プリファレンスがない場合は -1
    private fun getLastIndex(): Int {
        var lastIndex = -1
        lastIndex = sharedPrefBooks.getInt(historyLastIndexKeyStr, lastIndex)
        return lastIndex
    }

    private fun saveLastIndex(lastIndex: Int) {
        val editor = sharedPrefBooks.edit()
        editor.putInt(historyLastIndexKeyStr, lastIndex)
        editor.commit()
    }

    private fun saveHistory(
        lastIndex: Int,
        bookTitle: String,
        isbn: String
    ) {
        val editor = sharedPrefBooks.edit()
        val jsonArray = JSONArray()
        jsonArray.put(bookTitle) //本のタイトル
        jsonArray.put(isbn) //isbn
        editor.putString(Integer.toString(lastIndex), jsonArray.toString())
        editor.commit()
    }
}