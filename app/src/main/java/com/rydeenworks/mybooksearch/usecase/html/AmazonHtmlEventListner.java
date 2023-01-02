package com.rydeenworks.mybooksearch.usecase.html;

import com.rydeenworks.mybooksearch.domain.Book;

import java.util.EventListener;

public interface AmazonHtmlEventListner  extends EventListener {
    void OnFailedDownload();
    void OnSuccessDownload(Book book);
}
