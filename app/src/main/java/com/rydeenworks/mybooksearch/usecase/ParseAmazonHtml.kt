package com.rydeenworks.mybooksearch.usecase

import com.rydeenworks.mybooksearch.domain.Book
import java.util.regex.Pattern

class ParseAmazonHtml (){
    fun handle(httpSrc: String): Book
    {
        var bookTitle = ""
        val TITLE_TAG_START = "<title>"
        val TITLE_TAG_END = "</title>"
        val idxStart: Int = httpSrc.indexOf(TITLE_TAG_START)
        val idxEnd: Int = httpSrc.indexOf(TITLE_TAG_END)
        if (idxStart != -1 && idxEnd != -1) {
            bookTitle = httpSrc.substring(idxStart + TITLE_TAG_START.length, idxEnd)
            val idxSeparator = bookTitle.indexOf("|")
            if (idxSeparator != -1) {
                bookTitle = bookTitle.substring(0, idxSeparator)
            }
            bookTitle = bookTitle.trim { it <= ' ' }
            //        <title>忙しい人専用 「つくりおき食堂」の超簡単レシピ (扶桑社ムック) | 若菜 まりえ |本 | 通販 | Amazon</title>
        } else {
            val BOOK_TITLE_TAG = "<h1 id=\"title\" class=\"a-size-medium\">"
            val idx: Int = httpSrc.indexOf(BOOK_TITLE_TAG)
            if (idx != -1) {
//                    Log.d("AAA", "</title> found at " + idx);
                val idxLineEnd: Int = httpSrc.indexOf("\n", idx)
                if (idxLineEnd != -1) {
                    bookTitle = httpSrc.substring(idx + BOOK_TITLE_TAG.length, idxLineEnd)
                    //                        Log.d("AAA", httpSrc.substring(idx + BOOK_TITLE_TAG.length(), idxLineEnd));
                }
            }
        }

        if (bookTitle.isEmpty()) {
            return Book(String(), String())
        }

        val ISBN13_TAG = "ISBN-13"
        val idx: Int = httpSrc.indexOf(ISBN13_TAG)
        if (idx == -1) {
//            Log.d("AAA", "ISBN-13 not found");
            return Book(String(), String())
        }
        val isbnHtml: String = httpSrc.substring(idx, idx + 1000) //100文字以内でマッチングできるはず

        val p = Pattern.compile("[0-9]{3}-[0-9]{9}[0-9|X]")
        val m = p.matcher(isbnHtml)
        if (!m.find()) {
            return Book(String(), String())
        }

        val isbn = m.group()
        return Book(bookTitle, isbn)
    }
}