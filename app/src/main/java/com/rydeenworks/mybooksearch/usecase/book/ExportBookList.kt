package com.rydeenworks.mybooksearch.usecase.book

import android.app.Activity
import android.widget.Toast
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.MainActivity

class ExportBookList(
    private val activity: Activity,
    private val bookRepository: BookRepository
) {
    fun handle()
    {
        val history = bookRepository.getHistoryText()

        MainActivity.copyToClipboard(activity, "図書さがし履歴", history.toString())

        val toast: Toast = Toast.makeText(activity, "履歴をクリップボードにコピーしました", Toast.LENGTH_LONG)
        toast.show()
    }
}