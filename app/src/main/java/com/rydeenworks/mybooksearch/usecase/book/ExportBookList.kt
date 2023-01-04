package com.rydeenworks.mybooksearch.usecase.book

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.rydeenworks.mybooksearch.infrastructure.BookRepository

class ExportBookList(
    private val activity: Activity,
    private val bookRepository: BookRepository
) {
    fun handle()
    {
        val history = bookRepository.getHistoryText()

        copyToClipboard("図書さがし履歴", history.toString())

        val toast: Toast = Toast.makeText(activity, "履歴をクリップボードにコピーしました", Toast.LENGTH_LONG)
        toast.show()
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboardManager =
            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}