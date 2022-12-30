package com.rydeenworks.mybooksearch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements BookLoadEventListener {
    private WebView calilWebView;
    private final CalilWebViewClient calilWebViewClient = new CalilWebViewClient();
    private final HistoryPage historyPage = new HistoryPage();

    enum ViewMode{
        VIEW_MODE_HISTORY,
        VIEW_MODE_IMAGE,
    }
    private ViewMode mViewMode = ViewMode.VIEW_MODE_HISTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle("図書さがし");

        initCalilWebView();
        searchBookPage();
    }

    private void showReviewDialog() {
        new AlertDialog.Builder(this)
                .setTitle("レビューにご協力お願いします")
                .setMessage("いつもご利用ありがとうございます。よろしければ励ましのレビューをお寄せください")
                .setPositiveButton("レビューする", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(context.getString(R.string.app_is_reviewd), true);
                        editor.commit();

                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.rydeenworks.mybooksearch");
                        Intent i = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(i);
                    }
                })
                .setNeutralButton("その他要望", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(context.getString(R.string.app_is_approached), true);
                        editor.commit();

                        Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSegGYDdqtw9gq8xZSb4yqORgFE5A4uQzR0RBrFDtLfsIDfs3g/viewform?usp=sf_link");
                        Intent i = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(i);
                    }
                })
                .setNegativeButton("また今度", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //処理なし
                    }
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

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        Boolean isAppApproached = sharedPref.getBoolean(context.getString(R.string.app_is_approached), false);
        Boolean isAppReviewed = sharedPref.getBoolean(context.getString(R.string.app_is_reviewd), false);

        if( isAppApproached == false && isAppReviewed == false)
        {
            // 10冊検索するごとに表示する
            int num = historyPage.GetBookHistoryNum(this);
            if( num > 0 && (num % 10) == 0 ) {
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
        Intent i = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i);
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
                ArrayList<JSONArray> history = historyPage.GetHistory(this);

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
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String history = editText.getText().toString();
                            try {
                                JSONArray jarray = new JSONArray(history);
                                for (int i = jarray.length() - 1; i >= 0;  --i) {
//                                  Log.d("AAA", jarray.getString(i));
                                    JSONArray book = new JSONArray(jarray.getString(i));
//                                  Log.d("AAA", String.format("title:%s isbn:%s", book.getString(0), book.getString(1)));
                                    historyPage.AddHistory(getApplicationContext(), book.getString(0), book.getString(1));
                                }
                                showHistoryPage();
                            }
                            catch (org.json.JSONException e) {

                            }
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //処理なし
                        }
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
        calilWebView.post(new Runnable() {
            @Override
            public void run() {
                String htmlString = historyPage.GetWebPage(MainActivity.this);
                String encodedHtml = Base64.encodeToString(htmlString.getBytes(), Base64.DEFAULT);
                calilWebView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
            }
        });

        mViewMode = ViewMode.VIEW_MODE_HISTORY;
    }

    private void showBooksImagePage() {
        calilWebView.post(new Runnable() {
            @Override
            public void run() {
                int width = calilWebView.getWidth();
                String htmlString = historyPage.GetImagePage(MainActivity.this, width);
                String encodedHtml = Base64.encodeToString(htmlString.getBytes(), Base64.DEFAULT);
                calilWebView.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
            }
        });

        mViewMode = ViewMode.VIEW_MODE_IMAGE;
    }

    public Boolean OnLoadBookHttp(String httpSrc) {
        String bookTitle = "";
        {
            final String TITLE_TAG_START = "<title>";
            final String TITLE_TAG_END = "</title>";
            final int idxStart = httpSrc.indexOf(TITLE_TAG_START);
            final int idxEnd = httpSrc.indexOf(TITLE_TAG_END);

            if(idxStart != -1 && idxEnd != -1)
            {
                bookTitle = httpSrc.substring(idxStart + TITLE_TAG_START.length(), idxEnd);
                final int idxSeparator = bookTitle.indexOf("|");
                if( idxSeparator != -1) {
                    bookTitle = bookTitle.substring(0, idxSeparator);
                }
                bookTitle = bookTitle.trim();
//        <title>忙しい人専用 「つくりおき食堂」の超簡単レシピ (扶桑社ムック) | 若菜 まりえ |本 | 通販 | Amazon</title>
            }else{
                final String BOOK_TITLE_TAG = "<h1 id=\"title\" class=\"a-size-medium\">";
                final int idx = httpSrc.indexOf(BOOK_TITLE_TAG);
                if(idx != -1)
                {
//                    Log.d("AAA", "</title> found at " + idx);
                    final int idxLineEnd = httpSrc.indexOf("\n", idx);
                    if(idxLineEnd != -1)
                    {
                        bookTitle = httpSrc.substring(idx + BOOK_TITLE_TAG.length(), idxLineEnd);
//                        Log.d("AAA", httpSrc.substring(idx + BOOK_TITLE_TAG.length(), idxLineEnd));
                    }
                }
            }
        }

        if(bookTitle.isEmpty())
        {
            return false;
        }

        final String ISBN13_TAG = "ISBN-13";
        final int idx = httpSrc.indexOf(ISBN13_TAG);
        if(idx == -1) {
//            Log.d("AAA", "ISBN-13 not found");
            return false;
        }
        String isbnHtml = httpSrc.substring(idx, idx+1000);  //100文字以内でマッチングできるはず

        Pattern p = Pattern.compile("[0-9]{3}-[0-9]{9}[0-9|X]");
        Matcher m = p.matcher(isbnHtml);
        if (m.find()){
            String isbn = m.group();
            Uri uri = Uri.parse("https://calil.jp/book/" + isbn);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);

            historyPage.AddHistory(this, bookTitle, isbn);
            showHistoryPage();
            return true;
        }else{
            return false;
        }
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
