package com.rydeenworks.mybooksearch;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalilWebViewClient extends WebViewClient {
    private BookLoadEventListener eventListener;
    public void SetEventListener(BookLoadEventListener listener) {
        eventListener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, java.lang.String url) {
        Uri uri = Uri.parse(url);
        handleOverrideUrlLoading(uri);
        return true;
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
