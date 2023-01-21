package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSearchHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = BookSearchHistoryFragment()
    }

    private val viewModel: BookSearchHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                buildContent()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun buildContent()
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