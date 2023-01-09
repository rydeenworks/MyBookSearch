package com.rydeenworks.mybooksearch.usecase.booksearch;

import com.rydeenworks.mybooksearch.domain.Book;

import java.util.EventListener;

public interface AnalyzeBookPageEventListner extends EventListener {
    void OnFailedAnalyzingBookPage();
    void OnSuccessAnalyzingBookPage(Book book);
}
