package com.rydeenworks.mybooksearch.usecase.bookbackup

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.EditText
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import org.json.JSONArray
import org.json.JSONException

class ImportBookList(
    private val activity: Activity,
    private val bookRepository: BookRepository
) {
    fun handle()
    {
        val editText = EditText(activity)
        editText.hint = "ここへ書き出し内容を貼り付け"
        editText.height = 400
        AlertDialog.Builder(activity)
            .setTitle("履歴読み込み")
            .setMessage("履歴書き出し内容を貼り付けましょう")
            .setView(editText)
            .setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                val history1 = editText.text.toString()
                try {
                    val jarray = JSONArray(history1)
                    for (i in jarray.length() - 1 downTo 0) {
//                                  Log.d("AAA", jarray.getString(i));
                        val book = JSONArray(jarray.getString(i))
                        //                                  Log.d("AAA", String.format("title:%s isbn:%s", book.getString(0), book.getString(1)));
                        bookRepository.addBook(book.getString(0), book.getString(1))
                    }
                } catch (e: JSONException) {
                }
            }
            .setNegativeButton("キャンセル") { dialog: DialogInterface?, which: Int -> }
            .show()
    }
}