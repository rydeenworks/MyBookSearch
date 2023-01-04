package com.rydeenworks.mybooksearch.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.rydeenworks.mybooksearch.R;
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog;
import com.rydeenworks.mybooksearch.ui.historypage.HistoryPageWebView;
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage;
import com.rydeenworks.mybooksearch.ui.webview.BookClickEventListener;
import com.rydeenworks.mybooksearch.ui.webview.WebViewAdapter;
import com.rydeenworks.mybooksearch.usecase.book.SearchBookInLibrary;
import com.rydeenworks.mybooksearch.usecase.browser.OpenChromeBrowser;

public class MainActivity extends AppCompatActivity
        implements BookClickEventListener,
        BookRepositoryEventListner
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
        reviewDialog = new ReviewDialog(this);  // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある

        setContentView(R.layout.activity_main);

        if(historyPage == null)
        {
            WebView calilWebView = findViewById(R.id.webView_calil);
            WebViewAdapter webViewAdapter = new WebViewAdapter(calilWebView, this);
            historyPage = new HistoryPageWebView(webViewAdapter, bookRepository);
        }

        appMenu = new AppMenu(this, historyPage, bookRepository, reviewDialog);


//        setContentView(R.layout.book_search_history);

//        // データを用意
//        String[] data = {
//                "とり", "しか", "ぞう", "きつね",
//                "かば", "ライオン", "パンダ", "ひつじ"
//        };
//
//        // ListViewにデータをセットする
//        ListView list = findViewById(R.id.book_list);
//        list.setAdapter(new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                data
//        ));

        setTitle("図書さがし");

        historyPage.updateView();
    }

    @Override
    public void OnLinkClick(Uri uri) {
        OpenChromeBrowser openChromeBrowser = new OpenChromeBrowser(this);
        openChromeBrowser.handle(uri);
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
