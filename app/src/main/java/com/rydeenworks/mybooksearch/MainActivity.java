package com.rydeenworks.mybooksearch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements BookLoadEventListener {
    private WebView calilWebView;
    private final CalilWebViewClient calilWebViewClient = new CalilWebViewClient();
    private final HistoryPage historyPage = new HistoryPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setTitle("図書さがし");

        String admob_app_id = this.getString(R.string.admob_app_id);
        MobileAds.initialize(this, admob_app_id);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//        String adUnitId = mAdView.getAdUnitId();

        initCalilWebView();
        searchBookPage();;
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
                new AlertDialog.Builder(this)
                    .setTitle("履歴読み込み")
                    .setMessage("履歴書き出し内容を貼り付けましょう")
                    .setView(editText)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String history = editText.getText().toString();
                            Log.d("AAA", history);

                            try {
                                JSONArray jarray = new JSONArray(history);
                                for (int i = 0; i < jarray.length(); ++ i) {
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
                    .show();
                break;

            case R.id.menu_show_help_page:
                Uri uri = Uri.parse("https://rydeenworks.hatenablog.com/entry/2019/09/12/214733");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
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
        String htmlString = historyPage.GetWebPage(this);
        calilWebView.loadData(htmlString, "text/html", "utf-8");
    }

    public void OnLoadBookHttp(String httpSrc) {
        String bookTitle;
        {
            final String TITLE_TAG_START = "<title>";
            final String TITLE_TAG_END = "</title>";
            final int idxStart = httpSrc.indexOf(TITLE_TAG_START);
            if(idxStart == -1) {
                Toast toast = Toast.makeText(this, "このページは検索できませんでした", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            final int idxEnd = httpSrc.indexOf(TITLE_TAG_END);
            if(idxEnd == -1) {
                Toast toast = Toast.makeText(this, "このページは検索できませんでした", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            bookTitle = httpSrc.substring(idxStart + TITLE_TAG_START.length(), idxEnd);
            final int idxSeparator = bookTitle.indexOf("|");
            if( idxSeparator != -1) {
                bookTitle = bookTitle.substring(0, idxSeparator);
            }
            bookTitle = bookTitle.trim();
//        <title>忙しい人専用 「つくりおき食堂」の超簡単レシピ (扶桑社ムック) | 若菜 まりえ |本 | 通販 | Amazon</title>
        }

        final String ISBN13_TAG = "ISBN-13";
        final int idx = httpSrc.indexOf(ISBN13_TAG);
        if(idx == -1) {
            Toast toast = Toast.makeText(this, "このページは検索できませんでした", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String isbnHtml = httpSrc.substring(idx, idx+100);  //100文字以内でマッチングできるはず

        Pattern p = Pattern.compile("[0-9]{3}-[0-9]{9}[0-9|X]");
        Matcher m = p.matcher(isbnHtml);
        if (m.find()){
            String isbn = m.group();
            Uri uri = Uri.parse("https://calil.jp/book/" + isbn);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);

            historyPage.AddHistory(this, bookTitle, isbn);
            showHistoryPage();
        }else{
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
