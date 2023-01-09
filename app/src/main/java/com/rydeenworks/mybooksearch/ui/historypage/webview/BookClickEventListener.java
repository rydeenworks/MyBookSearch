package com.rydeenworks.mybooksearch.ui.historypage.webview;

import android.net.Uri;

import java.util.EventListener;

public interface BookClickEventListener extends EventListener {
    void OnLinkClick(Uri uri);
}
