package com.rydeenworks.mybooksearch.usecase;

import android.content.Context;
import android.content.SharedPreferences;

import com.rydeenworks.mybooksearch.R;
import com.rydeenworks.mybooksearch.domain.Book;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class HistoryPage {
    private final BookRepository bookRepository;
    private final SharedPreferences sharedPrefBooks;
    private final String historyLastIndexKeyStr;

    public HistoryPage(Context context)
    {
        sharedPrefBooks = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        historyLastIndexKeyStr = context.getString(R.string.history_last_index_key);
        bookRepository = new BookRepository(
                historyLastIndexKeyStr,
                sharedPrefBooks);
    }

    //本の履歴件数を返す( 0 - 99 ) 履歴がない場合は -1
    public int GetBookHistoryNum() {
        int retNum =bookRepository.getBookNum();
        return retNum;
    }

    public void AddHistory(String bookTitle, String isbn) {
        bookRepository.addBook(bookTitle, isbn);
    }

    public ArrayList<JSONArray> GetHistoryText() {
        return bookRepository.getHistoryText();
    }

    public List<Book> GetHistory() {
        return bookRepository.getHistoryList();
    }
}
