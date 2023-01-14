package com.rydeenworks.mybooksearch.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.databinding.FragmentFirstBinding
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.ui.historypage.listview.HistoryPageListView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//import com.rydeenworks.mybooksearch.ui.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment(), BookRepositoryEventListner
{
    private var _binding: FragmentFirstBinding? = null

    @Inject lateinit var bookRepository: BookRepository

    private lateinit var historyPage: IHistoryPage
    private lateinit var reviewDialog: ReviewDialog
    private lateinit var appMenu: AppMenu


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

//        val listView: ListView = view.findViewById(R.id.book_list)
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
        val context: Context = requireContext()
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE
        )
//        val historyLastIndexKeyStr = getString(R.string.history_last_index_key)
//        bookRepository = BookRepository(
//            historyLastIndexKeyStr,
//            sharedPref,
//            this
//        )
        reviewDialog = ReviewDialog(requireActivity(), sharedPref) // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある

        historyPage = HistoryPageListView(bookRepository, requireActivity(), listView)
        appMenu = AppMenu(requireActivity(), historyPage, bookRepository, reviewDialog)
        historyPage.updateView()
    }

    override fun onUpdateBookRepository() {
        historyPage.updateView()
    }
}