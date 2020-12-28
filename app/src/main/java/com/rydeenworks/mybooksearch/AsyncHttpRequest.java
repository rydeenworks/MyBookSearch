package com.rydeenworks.mybooksearch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AsyncHttpRequest extends AsyncTask<String, Void, Boolean> {
    private MainActivity mActivity;

    public AsyncHttpRequest(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Boolean success = false;
        String html = "";
        for(int i=0; i<10; i++){ // 10回ほどリトライする
            html = downloadHtml(params[0]);
            success = mActivity.OnLoadBookHttp(html);
            if(success) {
                break;
            }
        }
        return success;
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

    public void onPostExecute(Boolean result) {
        mActivity.NotifyBookSearchResul(result);
    }
}