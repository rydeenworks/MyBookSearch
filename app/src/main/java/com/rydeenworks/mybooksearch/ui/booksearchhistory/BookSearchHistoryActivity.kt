package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSearchHistoryActivity : ComponentActivity() {

    private val viewModel: BookSearchHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            BuildContent()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun BuildContent()
    {
        BookList()
    }

    @Composable
    fun BookList() {
        LazyColumn {
            items(viewModel.getBookList()) { book ->
                Row{
                    AsyncImage(
                        model = "https://cover.openbd.jp/" + book.isbn + ".jpg",
                        contentDescription = null,
                        modifier = Modifier.padding(all = 8.dp)
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
}