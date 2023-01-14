package com.rydeenworks.mybooksearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.rydeenworks.mybooksearch.databinding.FragmentFirstBinding
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.ui.historypage.listview.HistoryPageListView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment(), BookRepositoryEventListner
{
    private var _binding: FragmentFirstBinding? = null

    @Inject lateinit var bookRepository: BookRepository
    private lateinit var historyPage: IHistoryPage


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = binding.bookSearchHistoryLayout.bookList
        initView(listView)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(listView: ListView) {
        historyPage = HistoryPageListView(bookRepository, requireActivity(), listView)
        historyPage.updateView()
    }

    override fun onUpdateBookRepository() {
        historyPage.updateView()
    }
}