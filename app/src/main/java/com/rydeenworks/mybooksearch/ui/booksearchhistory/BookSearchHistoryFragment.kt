package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class BookSearchHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = BookSearchHistoryFragment()
    }

    private lateinit var viewModel: BookSearchHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookSearchHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                content()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun content()
    {
        Text(text = "Hello world.")
    }
}