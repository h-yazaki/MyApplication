package com.example.h_yazaki.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncJsonLoader  extends AsyncTask<String,Integer, JSONObject> {
    public interface AsyncCallback {
        void preExecute();
        void postExecute(JSONObject result);
        void progressUpdate(int progress);
        void cancel();
    }

    private static final String TAG = "TestJson2";
    private AsyncCallback mAsyncCallback = null;

    public AsyncJsonLoader(AsyncCallback _asyncCallback) {
        mAsyncCallback = _asyncCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mAsyncCallback != null) {
            mAsyncCallback.preExecute();
        }
    }

    @Override
    protected void onProgressUpdate(Integer ... _progress) {
        super.onProgressUpdate(_progress);
        if (mAsyncCallback != null) {
            mAsyncCallback.progressUpdate(_progress[0]);
        }
    }

    @Override
    protected void onPostExecute(JSONObject _result) {
        super.onPostExecute(_result);
        if (mAsyncCallback != null) {
            mAsyncCallback.postExecute(_result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mAsyncCallback != null) {
            mAsyncCallback.cancel();
        }
    }

    @Override
    protected JSONObject doInBackground(String... _uri) {
Log.d(TAG, "0 = "+_uri[0]);
        HttpURLConnection con = null;
        URL url = null;
        JSONObject jsonObject = null;

        try {
            // URLの作成
            url = new URL(_uri[0]);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection) url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("GET");

            Log.d(TAG, "InstanceFollowRedirects = " + con.getInstanceFollowRedirects());
            Log.d(TAG, "DoInput = " + con.getDoInput());
            Log.d(TAG, "DoOutput = " + con.getDoOutput());
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(false);

            // 接続
            con.connect(); // ①

Log.d(TAG, "1");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
Log.d(TAG, "2");
                // 本文の取得
                InputStream in = con.getInputStream();
                String strData = readInputStream(in);
                in.close();

                Log.d(TAG, strData);
                try {
                    jsonObject = new JSONObject(strData);
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException");
                    e.printStackTrace();
                }
                Log.d(TAG, "3");
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException");
            e.printStackTrace();
        }
        return jsonObject;

/*
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(_uri[0]);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(outputStream);
                outputStream.close();
                return new JSONObject(outputStream.toString());
            } else {
                httpResponse.getEntity().getContent().close();
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
*/
    }

    private String readInputStream(InputStream in)
            throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        while((st = br.readLine()) != null) {
            sb.append(st);
        }
        try {
            in.close();
        } catch (Exception e) {
        }
        return sb.toString();
    }

}
