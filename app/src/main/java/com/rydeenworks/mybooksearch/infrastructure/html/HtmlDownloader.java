package com.rydeenworks.mybooksearch.infrastructure.html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HtmlDownloader {
    public String downloadHtml(URI targetURL) {
        HttpURLConnection connection = null;
        StringBuilder src = new StringBuilder();
        try {
            URL url = targetURL.toURL();
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();

            while (true) {
                byte[] line = new byte[1024];
                int size = is.read(line);
                if (size <= 0)
                    break;
                String str = new String(line, StandardCharsets.UTF_8);
                src.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(connection != null) {
                connection.disconnect();
            }
        }
        return new String(src);
    }
}