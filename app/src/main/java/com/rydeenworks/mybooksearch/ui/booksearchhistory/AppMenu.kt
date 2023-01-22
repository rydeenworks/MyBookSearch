package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.app.Activity
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.usecase.bookbackup.ExportBookList
import com.rydeenworks.mybooksearch.usecase.bookbackup.ImportBookList
import com.rydeenworks.mybooksearch.usecase.customerservice.ShowAppHistoryPage
import com.rydeenworks.mybooksearch.usecase.customerservice.ShowHelpPage

class AppMenu(
    private val activity: Activity,
    private val bookRepository: BookRepository,
) {
    @Composable
    fun Menu()
    {
        var showMenu by remember { mutableStateOf(false) }
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(onClick = { exportHistory() }) {
                Text("履歴を書き出す")
            }
            DropdownMenuItem(onClick = { importHistory() }) {
                Text("履歴を読み込む")
            }
            DropdownMenuItem(onClick = { showHelpPage() }) {
                Text("ヘルプ")
            }
            DropdownMenuItem(onClick = { showAppReviewDialog()}) {
                Text("アプリのレビュー・要望")
            }
            DropdownMenuItem(onClick = { showAppHistoryPage() }) {
                Text("アプリ更新履歴")
            }
        }
    }

    private fun exportHistory()
    {
        ExportBookList(activity, bookRepository).handle()
    }

    private fun importHistory()
    {
        ImportBookList(activity, bookRepository).handle()
    }
    private fun showHelpPage()
    {
        ShowHelpPage(activity).handle()
    }
    private fun showAppReviewDialog()
    {
        ReviewDialog(activity).showDialog()
    }
    private fun showAppHistoryPage()
    {
        ShowAppHistoryPage(activity).handle()
    }
}