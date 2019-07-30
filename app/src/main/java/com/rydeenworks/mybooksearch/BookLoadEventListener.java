package com.rydeenworks.mybooksearch;

import android.net.Uri;

import java.util.EventListener;

public interface BookLoadEventListener extends EventListener {
    void OnLinkClick(Uri uri);
}
