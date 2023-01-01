package com.rydeenworks.mybooksearch.usecase

import org.json.JSONArray

class CreateBookListWebPage {

    fun handle(historyList: ArrayList<JSONArray>): String {
        val htmlBuilder = StringBuilder()
        htmlBuilder.append("<html>")
        htmlBuilder.append("<head>")
        htmlBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
        htmlBuilder.append("<style type=\"text/css\">")
        htmlBuilder.append(".header {")
        htmlBuilder.append("background-color: #bdf7f1;")
        htmlBuilder.append("}")
        htmlBuilder.append("a {  font-family: \"Helvetica Neue\", Arial, \"Hiragino Kaku Gothic ProN\", \"Hiragino Sans\", Meiryo, sans-serif;}")
        htmlBuilder.append("</style>")
        htmlBuilder.append("<style>img{display: inline;height: auto;max-width: 10%;} </style>")
        htmlBuilder.append("</head>")
        htmlBuilder.append("<body>")
        htmlBuilder.append("<div class=\"header\">")
        htmlBuilder.append("図書さがし履歴一覧")
        htmlBuilder.append("</div>")
        try {
            htmlBuilder.append("<br>")
            for (jsonArray in historyList) {
                htmlBuilder.append("<img align=\"left\" src=\"")
                val img_url = "https://cover.openbd.jp/" + jsonArray[1] + ".jpg\" "
                htmlBuilder.append(img_url)
                htmlBuilder.append("alt=\"" + jsonArray[0] + "\" style=\"border:solid 1px #000000\"")
                htmlBuilder.append(">")
                htmlBuilder.append("<a href=\"")
                val url =
                    "https://www.amazon.co.jp/gp/search?ie=UTF8&tag=dynamitecruis-22&linkCode=ur2&linkId=4b1da2ab20d2fa32b9230f88ddab039e&camp=247&creative=1211&index=books&keywords=" + jsonArray[0]
                htmlBuilder.append(url)
                htmlBuilder.append("\">")
                htmlBuilder.append(jsonArray[0])
                htmlBuilder.append("</a>")
                htmlBuilder.append("<br clear=\"left\"")
                htmlBuilder.append("<hr>")
                htmlBuilder.append("<hr>")
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