package com.rydeenworks.mybooksearch.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.rydeenworks.mybooksearch.R;
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog;
import com.rydeenworks.mybooksearch.ui.historypage.listview.HistoryPageListView;
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage;
import com.rydeenworks.mybooksearch.usecase.booksearch.SearchBookInLibrary;

public class MainActivity extends AppCompatActivity
        implements BookRepositoryEventListner
{
    private BookRepository bookRepository;
    private IHistoryPage historyPage;
    private ReviewDialog reviewDialog;
    private AppMenu appMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        searchBookPage();
    }

    private void searchBookPage() {
        SearchBookInLibrary searchBookInLibrary = new SearchBookInLibrary(this, bookRepository);
        if(searchBookInLibrary.handle())
        {
            int num = bookRepository.getBookNum();
            reviewDialog.showDialog(num);
        }
   }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        String historyLastIndexKeyStr = getString(R.string.history_last_index_key);
        bookRepository = new BookRepository(
                historyLastIndexKeyStr,
                sharedPref,
                this);
        reviewDialog = new ReviewDialog(this, sharedPref);  // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある

//        historyPage = new HistoryPageWebView(bookRepository, this);
        historyPage = new HistoryPageListView(bookRepository, this);

        appMenu = new AppMenu(this, historyPage, bookRepository, reviewDialog);

        historyPage.updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return appMenu.inflateMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return appMenu.onSelectMenu(item);
    }

    @Override
    public void onUpdateBookRepository() {
        historyPage.updateView();
    }
}
