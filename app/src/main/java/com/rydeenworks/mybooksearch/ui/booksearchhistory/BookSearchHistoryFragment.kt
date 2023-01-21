package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class BookSearchHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = BookSearchHistoryFragment()
    }

    private val viewModel: BookSearchHistoryViewModel by viewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // TODO: Use the ViewModel
//    }

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
        List1()
//        BookList()
    }

    @Composable
    fun List1() {
        LazyColumn {
            items(viewModel.getFluits()) { fruit ->
                Text(text = "This is $fruit")
            }
        }
    }

//    @Composable
//    fun BookList() {
//        LazyColumn {
//            items(viewModel.getBookList()) { book ->
//                Text(text = "This is ${book.title}")
//            }
//        }
//    }

}