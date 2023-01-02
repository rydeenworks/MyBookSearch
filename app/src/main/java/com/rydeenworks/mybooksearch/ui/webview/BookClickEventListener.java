package com.rydeenworks.mybooksearch.ui.webview;

import android.net.Uri;

import java.util.EventListener;

public interface BookClickEventListener extends EventListener {
    void OnLinkClick(Uri uri);
}
