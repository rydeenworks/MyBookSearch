package com.rydeenworks.mybooksearch;

import android.net.Uri;
import android.webkit.WebViewClient;

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
