package com.rydeenworks.mybooksearch.ui.historypage.webview;

import android.webkit.WebViewClient;

public class CalilWebViewClient extends WebViewClient {
    private BookClickEventListener eventListener;
    public void SetEventListener(BookClickEventListener listener) {
        eventListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, android.webkit.WebResourceRequest request) {
        eventListener.OnLinkClick(request.getUrl());
        return true;
    }
}
