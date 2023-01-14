package com.rydeenworks.mybooksearch.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.rydeenworks.mybooksearch.R
import com.rydeenworks.mybooksearch.databinding.ActivitySearchBookBinding
import com.rydeenworks.mybooksearch.infrastructure.BookRepository
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog
import com.rydeenworks.mybooksearch.usecase.booksearch.SearchBookInLibrary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//import com.rydeenworks.mybooksearch.databinding.ActivitySearchBookBinding
//import com.rydeenworks.mybooksearch.ui.databinding.ActivitySearchBookBinding

@AndroidEntryPoint
class SearchBookActivity: AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySearchBookBinding

    @Inject lateinit var bookRepository: BookRepository
    private lateinit var appMenu: AppMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_search_book)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        initView()
        searchBookPage()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_search_book)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return appMenu.inflateMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return appMenu.onSelectMenu(item)
    }

    private fun initView() {
       appMenu = AppMenu(this, bookRepository)
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