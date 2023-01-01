package com.rydeenworks.mybooksearch;

import android.content.Context;
import android.content.SharedPreferences;
import com.rydeenworks.mybooksearch.infrastructure.BookRepository;
import org.json.JSONArray;
import java.util.ArrayList;

public class HistoryPage {
    private final int HISTORY_MAX_NUM = 100;
    private final BookRepository bookRepository;
    private SharedPreferences sharedPrefBooks;
    private final String historyLastIndexKeyStr;

    public HistoryPage(Context context)
    {
        sharedPrefBooks = context.getSharedPreferences(
                context.getString(R.string.preference_history_file_key), Context.MODE_PRIVATE);
        historyLastIndexKeyStr = context.getString(R.string.history_last_index_key);
        bookRepository = new BookRepository(
                context.getString(R.string.history_last_index_key),
                sharedPrefBooks);
    }

    //本の履歴件数を返す( 0 - 99 ) 履歴がない場合は -1
    public int GetBookHistoryNum(Context context) {
        int num = getLastIndex();
        int retNum =bookRepository.getBookNum();
        return retNum;
    }


    public void AddHistory(Context context, String bookTitle, String isbn) {
        //登録済みの履歴は追加しない
        if( isExistHistoryBookTitle(context, sharedPrefBooks, bookTitle) ) {
            return;
        }

        int lastIndex = getLastIndex();
        lastIndex = incrementLastIndex(lastIndex);
        saveHistory(sharedPrefBooks, lastIndex, bookTitle, isbn);
        saveLastIndex(context, sharedPrefBooks, lastIndex);

        //test
//        ArrayList<JSONArray> historyList = getHistoryList(context, sharedPref);
//        try {
//            Integer idx=0;
//            for (JSONArray jsonArray : historyList) {
//                Log.d("history", idx.toString() + " bookTitle:" + jsonArray.get(0)+ " " + jsonArray.get(1) );
//                idx++;
//            }
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public ArrayList<JSONArray> GetHistory(Context context) {
        return getHistoryList(context, sharedPrefBooks);
    }

    private int incrementLastIndex(int lastIndex) {
        lastIndex++;
        if(lastIndex == HISTORY_MAX_NUM) {
            lastIndex = 0;
        }
        return lastIndex;
    }

    private int decrementLastIndex(int lastIndex) {
        lastIndex--;
        if(lastIndex == -1) {
            lastIndex = HISTORY_MAX_NUM -1;
        }
        return lastIndex;
    }

    // 0 <= lastIndex < HISTORY_MAX_NUM -1
    // プリファレンスがない場合は -1
    private int getLastIndex() {
        return  sharedPrefBooks.getInt(historyLastIndexKeyStr, -1);
    }

    private void saveLastIndex(Context context, SharedPreferences sharedPref, int lastIndex) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.history_last_index_key), lastIndex);
        editor.commit();
    }

    private void saveHistory(SharedPreferences sharedPref, int lastIndex, String bookTitle, String isbn) {
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(bookTitle); //本のタイトル
        jsonArray.put(isbn); //isbn
        editor.putString(Integer.toString(lastIndex), jsonArray.toString());
        editor.commit();
    }

    private ArrayList< JSONArray > getHistoryList(Context context, SharedPreferences sharedPref) {
        ArrayList<JSONArray> historyList = new ArrayList<>();

        final int lastIndex = getLastIndex();
        int currIndex = lastIndex;
        do {
            String strJson = sharedPref.getString(Integer.toString(currIndex), "");
            if(strJson == null) {
                break;
            }
            if(strJson.isEmpty()) {
                break;
            }
            try {
                JSONArray jsonAry = new JSONArray(strJson);
                historyList.add(jsonAry);
            } catch(Exception ex) {
                ex.printStackTrace();
                break;
            }
            currIndex = decrementLastIndex(currIndex);
        }while (currIndex != lastIndex);
        return  historyList;
    }

    private boolean isExistHistoryBookTitle(Context context, SharedPreferences sharedPref, final String bookTitle) {
        ArrayList<JSONArray> historyList = getHistoryList(context, sharedPref);
        try {
            for (JSONArray jsonArray : historyList) {
                if(bookTitle.equals(jsonArray.get(0))) {
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public String GetWebPage (Context context) {
        ArrayList<JSONArray> historyList = getHistoryList(context, sharedPrefBooks);
        return createHtml(context, historyList);
    }

    private String createHtml(Context context, ArrayList<JSONArray> historyList) {
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

    public String GetImagePage (Context context, int view_width) {
        ArrayList<JSONArray> historyList = getHistoryList(context, sharedPrefBooks);
        return createImageHtml(context, historyList, view_width);
    }


    private String createImageHtml(Context context, ArrayList<JSONArray> historyList, int viewWidth) {
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
