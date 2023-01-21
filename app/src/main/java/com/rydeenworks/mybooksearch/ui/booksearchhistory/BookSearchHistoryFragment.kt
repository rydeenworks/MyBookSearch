package com.rydeenworks.mybooksearch.ui.booksearchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rydeenworks.mybooksearch.databinding.FragmentBookSearchHistoryBinding

class BookSearchHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = BookSearchHistoryFragment()
    }

    private lateinit var viewModel: BookSearchHistoryViewModel

    private var _binding: FragmentBookSearchHistoryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookSearchHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSearchHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}