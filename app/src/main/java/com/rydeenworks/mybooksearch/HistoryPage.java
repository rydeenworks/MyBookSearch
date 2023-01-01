package com.rydeenworks.mybooksearch;

import android.content.Context;
import android.content.SharedPreferences;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import org.json.JSONArray;
import java.util.ArrayList;

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

    public ArrayList<JSONArray> GetHistory() {
        return bookRepository.getHistoryList();
    }

    public String GetWebPage () {
        ArrayList<JSONArray> historyList = bookRepository.getHistoryList();
        return createHtml(historyList);
    }

    private String createHtml(ArrayList<JSONArray> historyList) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlBuilder.append("<style type=\"text/css\">");
        htmlBuilder.append(".header {");
        htmlBuilder.append("background-color: #bdf7f1;");
        htmlBuilder.append("}");
        htmlBuilder.append("a {  font-family: \"Helvetica Neue\", Arial, \"Hiragino Kaku Gothic ProN\", \"Hiragino Sans\", Meiryo, sans-serif;}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("<style>img{display: inline;height: auto;max-width: 10%;} </style>");
        htmlBuilder.append("</head>");

        htmlBuilder.append("<body>");
        htmlBuilder.append("<div class=\"header\">");
        htmlBuilder.append("図書さがし履歴一覧");
        htmlBuilder.append("</div>");
        try {
            htmlBuilder.append("<br>");
            for (JSONArray jsonArray : historyList) {
                htmlBuilder.append("<img align=\"left\" src=\"");
                String img_url = "https://cover.openbd.jp/" + jsonArray.get(1) + ".jpg\" ";
                htmlBuilder.append(img_url);
                htmlBuilder.append("alt=\"" + jsonArray.get(0) + "\" style=\"border:solid 1px #000000\"");
                htmlBuilder.append(">");
                htmlBuilder.append("<a href=\"");
                String url = "https://www.amazon.co.jp/gp/search?ie=UTF8&tag=dynamitecruis-22&linkCode=ur2&linkId=4b1da2ab20d2fa32b9230f88ddab039e&camp=247&creative=1211&index=books&keywords=" + jsonArray.get(0);
                htmlBuilder.append(url);
                htmlBuilder.append("\">");
                htmlBuilder.append(jsonArray.get(0));
                htmlBuilder.append("</a>");
                htmlBuilder.append("<br clear=\"left\"");
                htmlBuilder.append("<hr>");
                htmlBuilder.append("<hr>");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            return "";
        }

        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    public String GetImagePage () {
        ArrayList<JSONArray> historyList = bookRepository.getHistoryList();
        return createImageHtml(historyList);
    }


    private String createImageHtml( ArrayList<JSONArray> historyList) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlBuilder.append("<style type=\"text/css\">");
        htmlBuilder.append(".header {");
        htmlBuilder.append("background-color: #bdf7f1;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("<style>img{display: inline;height: auto;max-width: 18%;}</style>");
        htmlBuilder.append("</head>");

        htmlBuilder.append("<body>");
        try {
            int count=0;
            final int BOOK_NUM = 5;
            for (JSONArray jsonArray : historyList) {
                if(count==0) {

                    htmlBuilder.append("<div style=\"text-align:center;\">");
                }
                htmlBuilder.append("<img src=\"");
                String url = "https://cover.openbd.jp/" + jsonArray.get(1) + ".jpg\" ";
                htmlBuilder.append(url);
                htmlBuilder.append("alt=\"" + jsonArray.get(0) + "\" style=\"border:solid 1px #000000\"");
                htmlBuilder.append(">");
                count++;
                if(count == BOOK_NUM){
                    htmlBuilder.append("</div>");
                    count = 0;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            return "";
        }

        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }
}
