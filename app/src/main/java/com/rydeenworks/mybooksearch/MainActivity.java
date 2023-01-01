package com.rydeenworks.mybooksearch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rydeenworks.mybooksearch.domain.Book;
import com.rydeenworks.mybooksearch.infrastructure.AsyncHttpRequest;
import com.rydeenworks.mybooksearch.usecase.CreateBookImageWebPage;
import com.rydeenworks.mybooksearch.usecase.CreateBookListWebPage;
import com.rydeenworks.mybooksearch.usecase.CustomerStatusService;
import com.rydeenworks.mybooksearch.usecase.ParseAmazonHtml;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookLoadEventListener {
    private WebView calilWebView;
    private final CalilWebViewClient calilWebViewClient = new CalilWebViewClient();
    private HistoryPage historyPage;
    private CustomerStatusService customerStatusService;

    enum ViewMode{
        VIEW_MODE_HISTORY,
        VIEW_MODE_IMAGE,
    }
    private ViewMode mViewMode = ViewMode.VIEW_MODE_HISTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyPage = new HistoryPage(this);

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        customerStatusService = new CustomerStatusService(
                sharedPref,
                context.getString(R.string.app_is_reviewd));


        setContentView(R.layout.activity_main);

        setTitle("図書さがし");

        initCalilWebView();
        searchBookPage();
    }

    private void showReviewDialog() {
        new AlertDialog.Builder(this)
                .setTitle("レビューにご協力お願いします")
                .setMessage("いつもご利用ありがとうございます。よろしければ励ましのレビューをお寄せください")
                .setPositiveButton("レビューする", (dialog, which) -> {
                    customerStatusService.saveAppReviewFlag();

                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.rydeenworks.mybooksearch");
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                })
                .setNeutralButton("その他要望", (dialog, which) -> {
                    Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSegGYDdqtw9gq8xZSb4yqORgFE5A4uQzR0RBrFDtLfsIDfs3g/viewform?usp=sf_link");
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                })
                .setNegativeButton("また今度", (dialog, which) -> {
                    //処理なし
                })
                .show();

    }

    private void searchBookPage() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        String bookPageUrl = null;
        if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
            bookPageUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        if (bookPageUrl == null) {
            return;
        }

        if( !customerStatusService.isAppReviewed())
        {
            // 4冊検索するごとに表示する
            int num = historyPage.GetBookHistoryNum();
            if( num > 0 && (num % 4) == 0 ) {
                showReviewDialog();
            }
        }

        Toast toast = Toast.makeText(this, "本を検索中・・・", Toast.LENGTH_LONG);
        toast.show();

        new AsyncHttpRequest(this).execute(bookPageUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCalilWebView() {
        if(calilWebView == null) {
            calilWebView = findViewById(R.id.webView_calil);
            calilWebView.setWebViewClient(calilWebViewClient);
            calilWebViewClient.SetEventListener(this);
            showHistoryPage();
        }
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
                ArrayList<JSONArray> history = historyPage.GetHistory();

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
                                historyPage.AddHistory(book.getString(0), book.getString(1));
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
                showReviewDialog();
                break;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 端末の戻るボタンでブラウザバック
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
            WebView webView = calilWebView;
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode,  event);
    }

    private void showHistoryPage() {
        calilWebView.post(() -> {
            ArrayList<JSONArray> books =  historyPage.GetHistory();
            CreateBookListWebPage createBookListWebPage = new CreateBookListWebPage();
            String htmlString = createBookListWebPage.handle(books);

            String encodedHtml = Base64.encodeToString(htmlString.getBytes(), Base64.DEFAULT);
            calilWebView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
        });

        mViewMode = ViewMode.VIEW_MODE_HISTORY;
    }

    private void showBooksImagePage() {
        calilWebView.post(() -> {
            ArrayList<JSONArray> books =  historyPage.GetHistory();
            CreateBookImageWebPage createBookImageWebPage = new CreateBookImageWebPage();
            String htmlString = createBookImageWebPage.handle(books);

            String encodedHtml = Base64.encodeToString(htmlString.getBytes(), Base64.DEFAULT);
            calilWebView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
        });

        mViewMode = ViewMode.VIEW_MODE_IMAGE;
    }

    public Boolean OnLoadBookHttp(String httpSrc) {
        ParseAmazonHtml amazonParser = new ParseAmazonHtml();
        Book book = amazonParser.handle(httpSrc);
        if(!book.isValid())
        {
            return false;
        }

        Uri uri = Uri.parse("https://calil.jp/book/" + book.getIsbn());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

        historyPage.AddHistory(book.getTitle(), book.getIsbn());
        showHistoryPage();
        return true;
    }

    public void NotifyBookSearchResul(Boolean success) {
        if(!success) {
            Toast toast = Toast.makeText(this, "このページは検索できませんでした", Toast.LENGTH_LONG);
            toast.show();
        }
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
}
