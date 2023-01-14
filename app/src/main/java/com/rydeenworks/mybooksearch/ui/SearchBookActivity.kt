package com.rydeenworks.mybooksearch.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.databinding.ActivitySearchBookBinding
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage
import com.rydeenworks.mybooksearch.ui.historypage.listview.HistoryPageListView
import dagger.hilt.android.AndroidEntryPoint

//import com.rydeenworks.mybooksearch.databinding.ActivitySearchBookBinding
//import com.rydeenworks.mybooksearch.ui.databinding.ActivitySearchBookBinding

@AndroidEntryPoint
class SearchBookActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySearchBookBinding

//    private lateinit var bookRepository: BookRepository
//    private lateinit var historyPage: IHistoryPage
//    private lateinit var reviewDialog: ReviewDialog
//    private lateinit var appMenu: AppMenu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_search_book)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

//        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_search_book)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        return appMenu.inflateMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return appMenu.onSelectMenu(item)
//    }

//    private fun initView(listView: ListView) {
//        val context: Context = requireContext()
//        val sharedPref = context.getSharedPreferences(
//            context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE
//        )
//        val historyLastIndexKeyStr = getString(R.string.history_last_index_key)
//        bookRepository = BookRepository(
//            historyLastIndexKeyStr,
//            sharedPref,
//            this
//        )
//        reviewDialog = ReviewDialog(this, sharedPref) // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある
//
//        historyPage = HistoryPageListView(bookRepository, this, listView)
//        appMenu = AppMenu(this, historyPage, bookRepository, reviewDialog)
//        historyPage.updateView()
//    }

}