package com.rydeenworks.mybooksearch.ui.webview;

import android.net.Uri;
import android.webkit.WebViewClient;

import com.rydeenworks.mybooksearch.usecase.BookLoadEventListener;

public class CalilWebViewClient extends WebViewClient {
    private BookLoadEventListener eventListener;
    public void SetEventListener(BookLoadEventListener listener) {
        eventListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, android.webkit.WebResourceRequest request) {
        handleOverrideUrlLoading(request.getUrl());
        return true;
    }

    private void handleOverrideUrlLoading(Uri uri ){
        eventListener.OnLinkClick(uri);
    }
}
