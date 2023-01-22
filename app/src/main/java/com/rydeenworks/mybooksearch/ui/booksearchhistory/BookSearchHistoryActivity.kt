package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.bookexplorer.BookExplorerActivity
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.usecase.booksearch.SearchBookInAmazon
import com.rydeenworks.mybooksearch.usecase.booksearch.SearchBookInLibrary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookSearchHistoryActivity : ComponentActivity() {

    @Inject
    lateinit var bookRepository: BookRepository

    private val viewModel: BookSearchHistoryViewModel by viewModels()
    private lateinit var appMenu: AppMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appMenu = AppMenu(this, bookRepository)

        setContent{
            BuildContent()
        }

        searchBookPage()
    }

    @Preview(showSystemUi = true)
    @Composable
    fun BuildContent()
    {
        Column {
            TopAppBarSample()
            BookList()
        }
    }

    @Composable
    fun BookList() {
        LazyColumn {
            items(viewModel.getBookList()) { book ->
                Row(
                    modifier = Modifier.clickable {
//                        SearchBookInAmazon(this@BookSearchHistoryActivity, bookRepository).handle(book)
                        val intent = Intent(this@BookSearchHistoryActivity, BookExplorerActivity::class.java).apply {
                            putExtra(getString(R.string.book_isbn_key), book.isbn)
                        }
                        startActivity(intent)
                        }
                ){
                    AsyncImage(
                        model = book.getCoverImageUrl(),
                        contentDescription = null,
                        modifier = Modifier.padding(all = 8.dp),
                        contentScale = FixedScale(0.5f)
                    )
                    Text(
                        text = book.title,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                }
                Divider(
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }

    @Composable
    fun TopAppBarSample() {
        TopAppBar(
            title = { Text("図書さがし") },
            actions = {
                appMenu.Menu()
            }
        )
    }

    private fun searchBookPage() {
        val searchBookInLibrary = SearchBookInLibrary(this, bookRepository)
        if (searchBookInLibrary.handle()) {
            val num = bookRepository.getBookNum()
            val reviewDialog = ReviewDialog(this)
            reviewDialog.showDialog(num)
        }
    }
}