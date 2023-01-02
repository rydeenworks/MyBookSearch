package com.rydeenworks.mybooksearch.infrastructure.html;

import java.util.EventListener;

public interface HtmlDownloadEventListner extends EventListener {
    Boolean OnDownloaded(String html);
    void OnFailedDownload();
    void OnSuccessDownload();
}
