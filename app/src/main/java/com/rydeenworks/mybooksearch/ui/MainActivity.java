package com.rydeenworks.mybooksearch.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import com.rydeenworks.mybooksearch.ui.customerservice.ReviewDialog;
import com.rydeenworks.mybooksearch.ui.historypage.HistoryPageWebView;
import com.rydeenworks.mybooksearch.ui.historypage.IHistoryPage;
import com.rydeenworks.mybooksearch.ui.webview.BookClickEventListener;
import com.rydeenworks.mybooksearch.R;
import com.rydeenworks.mybooksearch.domain.Book;
import com.rydeenworks.mybooksearch.ui.webview.WebViewAdapter;
import com.rydeenworks.mybooksearch.usecase.customerservice.CustomerStatusService;
import com.rydeenworks.mybooksearch.usecase.html.AmazonHtmlEventListner;
import com.rydeenworks.mybooksearch.usecase.html.DownloadAmazonHtmlService;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BookClickEventListener, AmazonHtmlEventListner {
    private BookRepository bookRepository;
    private IHistoryPage historyPage;
    private ReviewDialog reviewDialog;
    private DownloadAmazonHtmlService downloadAmazonHtmlService = new DownloadAmazonHtmlService(this);

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
                sharedPref);
        reviewDialog = new ReviewDialog(this);  // ダイアログ表示のためにMainActivity由来のContextを渡す必要がある

        initView();
        searchBookPage();
    }

    private void searchBookPage() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) == false)
        {
            return;
        }
        if(type == null)
        {
            return;
        }
        if("text/plain".equals(type) == false)
        {
            return;
        }

        String bookPageUrl = intent.getStringExtra(Intent.EXTRA_TEXT);

        int num = bookRepository.getBookNum();
        reviewDialog.showDialog(num);

        Toast toast = Toast.makeText(this, "本を検索中・・・", Toast.LENGTH_LONG);
        toast.show();

        downloadAmazonHtmlService.download(bookPageUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("ClickableViewAccessibility")
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
                ArrayList<JSONArray> history = bookRepository.getHistoryText();

                copyToClipboard(this, "図書さがし履歴", history.toString());

                Toast toast = Toast.makeText(this, "履歴をクリップボードにコピーしました", Toast.LENGTH_LONG);
                toast.show();

                break;
            case R.id.menu_import_history:
                final EditText editText = new EditText(this);
                editText.setHint("ここへ書き出し内容を貼り付け");
                editText.setHeight(400);
                new AlertDialog.Builder(this)
                    .setTitle("履歴読み込み")
                    .setMessage("履歴書き出し内容を貼り付けましょう")
                    .setView(editText)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String history1 = editText.getText().toString();
                        try {
                            JSONArray jarray = new JSONArray(history1);
                            for (int i = jarray.length() - 1; i >= 0;  --i) {
//                                  Log.d("AAA", jarray.getString(i));
                                JSONArray book = new JSONArray(jarray.getString(i));
//                                  Log.d("AAA", String.format("title:%s isbn:%s", book.getString(0), book.getString(1)));
                                bookRepository.addBook(book.getString(0), book.getString(1));
                            }
                            showHistoryPage();
                        }
                        catch (org.json.JSONException e) {

                        }
                    })
                    .setNegativeButton("キャンセル", (dialog, which) -> {
                        //処理なし
                    })
                    .show();
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
                Uri uri = Uri.parse("https://rydeenworks.hatenablog.com/entry/2019/09/12/214733");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
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
    public void OnFailedDownload() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Toast toast = Toast.makeText(this, "このページは検索できませんでした", Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    public void OnSuccessDownload(Book book) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Uri uri = Uri.parse("https://calil.jp/book/" + book.getIsbn());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);

            bookRepository.addBook(book.getTitle(), book.getIsbn());
            showHistoryPage();
        });
    }
}
