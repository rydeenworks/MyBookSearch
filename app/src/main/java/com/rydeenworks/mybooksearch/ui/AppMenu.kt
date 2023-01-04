package com.rydeenworks.mybooksearch.ui

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.usecase.book.ExportBookList
import com.rydeenworks.mybooksearch.usecase.book.ImportBookList
import com.rydeenworks.mybooksearch.usecase.customerservice.ShowHelpPage

class AppMenu(
    private val activity: Activity,
    private val historyPage: IHistoryPage,
    private val bookRepository: BookRepository,
    private val reviewDialog: ReviewDialog,
) {
    fun inflateMenu(menu: Menu): Boolean
    {
        activity.getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    fun onSelectMenu(item: MenuItem): Boolean
    {
        when (item.getItemId()) {
            R.id.menu_export_history -> {
                val exportBookList = ExportBookList(activity, bookRepository)
                exportBookList.handle()
            }
            R.id.menu_import_history -> {
                val importBookList = ImportBookList(activity, bookRepository)
                importBookList.handle()
            }
            R.id.print_books_image -> historyPage.togglePageStyle()
            R.id.menu_show_help_page -> {
                val showHelpPage = ShowHelpPage(activity)
                showHelpPage.handle()
            }
            R.id.menu_app_review -> reviewDialog.showDialog()
        }
        return true
    }
}