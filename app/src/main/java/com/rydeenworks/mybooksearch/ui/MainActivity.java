package com.rydeenworks.mybooksearch.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rydeenworks.mybooksearch.R;
import com.rydeenworks.mybooksearch.domain.BookRepositoryEventListner;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog;
import com.rydeenworks.mybooksearch.ui.historypage.HistoryPageWebView;
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage;
import com.rydeenworks.mybooksearch.ui.webview.BookClickEventListener;
import com.rydeenworks.mybooksearch.ui.webview.WebViewAdapter;
import com.rydeenworks.mybooksearch.usecase.book.ExportBookList;
import com.rydeenworks.mybooksearch.usecase.book.ImportBookList;
import com.rydeenworks.mybooksearch.usecase.book.SearchBookInLibrary;
import com.rydeenworks.mybooksearch.usecase.book.ShowHelpPage;

public class MainActivity extends AppCompatActivity
        implements BookClickEventListener,
        BookRepositoryEventListner
{
    private BookRepository bookRepository;
    private IHistoryPage historyPage;
    private ReviewDialog reviewDialog;

    enum ViewMode{
        VIEW_MODE_HISTORY,
        VIEW_MODE_IMAGE,
    }
    private ViewMode mViewMode = ViewMode.VIEW_MODE_HISTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        String historyLastIndexKeyStr = getString(R.string.history_last_index_key);
        bookRepository = new BookRepository(
                historyLastIndexKeyStr,
                sharedPref,
                this);
        reviewDialog = new ReviewDialog(this);  // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある

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
        setContentView(R.layout.activity_main);

        if(historyPage == null)
        {
            WebView calilWebView = findViewById(R.id.webView_calil);
            WebViewAdapter webViewAdapter = new WebViewAdapter(calilWebView, this);
            historyPage = new HistoryPageWebView(webViewAdapter);
        }

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

        showHistoryPage();
    }

    @Override
    public void OnLinkClick(Uri uri) {
        try {
            Intent i = new Intent();
            i.setPackage("com.android.chrome");
            i.setAction(Intent.ACTION_VIEW);
            i.setData(uri);
            startActivity(i);
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "ブラウザ起動に失敗しました", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_export_history:
                ExportBookList exportBookList = new ExportBookList(this, bookRepository);
                exportBookList.handle();
                break;
            case R.id.menu_import_history:
                ImportBookList importBookList = new ImportBookList(this, bookRepository);
                importBookList.handle();
                break;
            case R.id.print_books_image:
                switch (mViewMode) {
                    case VIEW_MODE_HISTORY:
                        showBooksImagePage();
                        break;
                    case VIEW_MODE_IMAGE:
                        showHistoryPage();
                        break;
                }
                break;
            case R.id.menu_show_help_page:
                ShowHelpPage showHelpPage = new ShowHelpPage(this);
                showHelpPage.handle();
                break;
            case R.id.menu_app_review:
                reviewDialog.showDialog();
                break;
        }
        return true;
    }

    private void showHistoryPage() {
        historyPage.showBookList(bookRepository.getHistoryList());
        mViewMode = ViewMode.VIEW_MODE_HISTORY;
    }

    private void showBooksImagePage() {
        historyPage.showBookImage(bookRepository.getHistoryList());
        mViewMode = ViewMode.VIEW_MODE_IMAGE;
    }

    public static void copyToClipboard(Context context, String label, String text) {
        // copy to clipboard
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == clipboardManager) {
            return;
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    @Override
    public void onUpdateBookRepository() {
        showHistoryPage();
    }
}
