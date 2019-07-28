package com.rydeenworks.mybooksearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rydeenworks.mybooksearch.MainActivity.ViewLayout.*;


public class MainActivity extends AppCompatActivity implements BookLoadEventListener {
    enum ViewLayout {
        VIEW_LAYOUT_MULTI,
        VIEW_LAYOUT_BOOK,
        VIEW_LAYOUT_CALIL,
    }
//    private ViewLayout viewLayout = VIEW_LAYOUT_MULTI;
    private ViewLayout viewLayout = VIEW_LAYOUT_CALIL;

    private WebView bookWebView;
    private WebView calilWebView;
    private final BookWebViewClient bookWebViewClient = new BookWebViewClient();
    private final CalilWebViewClient calilWebViewClient = new CalilWebViewClient();
    private WebView focusedView;

    private String lastBookWebUrl;

    private AdView mAdView;

    private final HistoryPage historyPage = new HistoryPage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "@string/admob_app_id");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        initCalilWebView();

        initBookWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if ( bookWebView != null ) {
//            // Get intent, action and MIME type
//            Intent intent = getIntent();
//            String action = intent.getAction();
//            String type = intent.getType();
//
//            String bookPageUrl = "https://www.google.co.jp";
//            if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
//                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//                if (sharedText != null) {
//                    bookPageUrl = sharedText;
//                }
//            }
//
//            //異なるページの場合は表示更新する
//            if ( lastBookWebUrl == null || !lastBookWebUrl.equals(bookPageUrl)) {
//                bookWebView.loadUrl(bookPageUrl);
//                bookWebView.requestFocus(View.FOCUS_DOWN | View.FOCUS_UP);
//                lastBookWebUrl = bookPageUrl;
//
//                if( calilWebView != null ) {
//                    calilWebView.loadUrl("https://calil.jp");
//                    calilWebView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
//                }
//            }
//        }

            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            String bookPageUrl = null;
            if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    bookPageUrl = sharedText;
                }
            }

            if (bookPageUrl == null) {
                return;
            }

            Toast toast = Toast.makeText(this, "本を検索中・・・", Toast.LENGTH_LONG);
            toast.show();

            new AsyncHttpRequest(this).execute(bookPageUrl);
    }

    private void initBookWebView() {
        if(bookWebView == null) {
            bookWebView = (WebView) findViewById(R.id.webView_main);
            bookWebView.setWebViewClient(bookWebViewClient);
            bookWebView.getSettings().setJavaScriptEnabled(true);
            bookWebView.setFocusable(true);
            bookWebView.setFocusableInTouchMode(true);
            bookWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            if (!v.hasFocus()) {
                                v.requestFocus();
                                OnViewFocus(v);
                            }
                            break;
                    }
                    return false;
                }
            });

            bookWebViewClient.SetEventListener(this);
            bookWebView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCalilWebView() {
        if(calilWebView == null) {
            calilWebView = (WebView) findViewById(R.id.webView_calil);
            //リンクをタップしたときに標準ブラウザを起動させない
            calilWebView.setWebViewClient(calilWebViewClient);
            //javascriptを許可する
            calilWebView.getSettings().setJavaScriptEnabled(true);
            calilWebView.setFocusable(true);
            calilWebView.setFocusableInTouchMode(true);
            calilWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            if (!v.hasFocus()) {
                                v.requestFocus();
                                OnViewFocus(v);
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void OnBookLoad(String bookTitle, String url) {
        if( bookTitle != null ) {
            //ローカルページは除外
            if ( bookTitle.contains(getString(R.string.web_page_title) ) ) {
                return;
            }

            calilWebViewClient.SetFormString(calilWebView, bookTitle);
            Toast toast = Toast.makeText(this, bookTitle, Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "本のタイトルが取れませんでした", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void OnAddAmazonBookHistory(String bookTitle, String isbn10) {
        historyPage.AddHistory(this, bookTitle, isbn10);
    }

    private void OnViewFocus(View view)
    {
        if(view == bookWebView) {
            focusedView = (WebView) view;
        } else if (view == calilWebView) {
            focusedView = (WebView) view;
        } else {
            focusedView = null;
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
//            case R.id.menu_show_history_page:
//                showHistoryPage();
//                break;
            case R.id.menu_show_help_page:
//                showRecommendPage();
                break;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 端末の戻るボタンでブラウザバック
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
            WebView webView = null;
            switch (viewLayout) {
                case VIEW_LAYOUT_MULTI:
                    webView = focusedView;
                    break;
                case VIEW_LAYOUT_BOOK:
                    webView = bookWebView;
                    break;
                case VIEW_LAYOUT_CALIL:
                    webView = calilWebView;
                    break;
            }
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode,  event);
    }

//    private void showHistoryPage() {
//        String htmlString = historyPage.GetWebPage(this);
//        bookWebView.loadData(htmlString, "text/html", "utf-8");
//    }

    public void OnLoadBookHttp(String httpSrc) {
        final String ISBN13_TAG = "ISBN-13";
        final int idx = httpSrc.indexOf(ISBN13_TAG);
        if(idx == -1) {
            return;
        }
        String isbnHtml = httpSrc.substring(idx, idx+100);  //100文字以内でマッチングできるはず

        Pattern p = Pattern.compile("[0-9]{3}-[0-9]{9}[0-9|X]");
        Matcher m = p.matcher(isbnHtml);
        if (m.find()){
            String matchstr = m.group();
            if( calilWebView != null ) {
                calilWebView.loadUrl("https://calil.jp/book/" + matchstr);
                calilWebView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
            }

        }
    }
}
