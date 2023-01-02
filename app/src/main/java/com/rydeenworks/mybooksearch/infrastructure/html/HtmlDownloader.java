package com.rydeenworks.mybooksearch.infrastructure.html;

import android.os.Handler;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HtmlDownloader {
    private class AsyncRunnable implements Runnable {

        private Boolean result;
        final private String _url;

        public AsyncRunnable(String url)
        {
            _url = url;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void run() {
            onPreExecute();
            result = doInBackground(_url);
            handler.post(() -> onPostExecute(result));
        }
    }

    private HtmlDownloadEventListner _eventListner;

    public HtmlDownloader(HtmlDownloadEventListner eventListner)
    {
        _eventListner = eventListner;
    }

    public void execute(String url)
    {
        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        executorService.submit(new AsyncRunnable(url));
    }

    public void onPreExecute() {
    }

    protected Boolean doInBackground(String params) {
        for(int i=0; i<10; i++){ // 10回ほどリトライする
            String html = downloadHtml(params);
            Boolean successParse = _eventListner.OnDownloaded(html);
            if(successParse) {
                return true;
            }
        }
        return false;
    }

    public String downloadHtml(String targetURL) {
        HttpURLConnection connection = null;
        StringBuilder src = new StringBuilder();
        try {
            URL url = new URL(targetURL);
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

    public void onPostExecute(Boolean result)
    {
        if(result)
        {
            _eventListner.OnSuccessDownload();
        }else{
            _eventListner.OnFailedDownload();
        }
    }
}