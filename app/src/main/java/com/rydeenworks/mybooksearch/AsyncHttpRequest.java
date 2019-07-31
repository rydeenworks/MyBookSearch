package com.rydeenworks.mybooksearch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AsyncHttpRequest extends AsyncTask<String, Void, String> {
    private MainActivity mActivity;

    public AsyncHttpRequest(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        StringBuilder src = new StringBuilder();
        try {
            URL url = new URL(params[0]);
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

    public void onPostExecute(String string) {
        mActivity.OnLoadBookHttp(string);
    }
}