package com.rydeenworks.mybooksearch.ui.booksearchhistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydeenworks.mybooksearch.R

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
    ): View {
        return inflater.inflate(R.layout.fragment_book_search_history, container, false)
    }

}