package com.rydeenworks.mybooksearch.usecase

import org.json.JSONArray

class CreateBookImageWebPage {
    fun handle(historyList: ArrayList<JSONArray>): String {
        val htmlBuilder = StringBuilder()
        htmlBuilder.append("<html>")
        htmlBuilder.append("<head>")
        htmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
        htmlBuilder.append("<style type=\"text/css\">")
        htmlBuilder.append(".header {")
        htmlBuilder.append("background-color: #bdf7f1;")
        htmlBuilder.append("}")
        htmlBuilder.append("</style>")
        htmlBuilder.append("<style>img{display: inline;height: auto;max-width: 18%;}</style>")
        htmlBuilder.append("</head>")
        htmlBuilder.append("<body>")
        try {
            var count = 0
            val BOOK_NUM = 5
            for (jsonArray in historyList) {
                if (count == 0) {
                    htmlBuilder.append("<div style=\"text-align:center;\">")
                }
                htmlBuilder.append("<img src=\"")
                val url = "https://cover.openbd.jp/" + jsonArray[1] + ".jpg\" "
                htmlBuilder.append(url)
                htmlBuilder.append("alt=\"" + jsonArray[0] + "\" style=\"border:solid 1px #000000\"")
                htmlBuilder.append(">")
                count++
                if (count == BOOK_NUM) {
                    htmlBuilder.append("</div>")
                    count = 0
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
        htmlBuilder.append("</body>")
        htmlBuilder.append("</html>")
        return htmlBuilder.toString()
    }
}