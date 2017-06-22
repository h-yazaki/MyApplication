package com.example.h_yazaki.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private String TAG = "TestJson";

    private String jsonData = "{" +
            "    datas:[" +
            "        {" +
            "            name : \"名前1\"," +
            "            age : \"年齢1\"" +
            "        }," +
            "        {" +
            "            name : \"名前2\"," +
            "            age : \"年齢2\"" +
            "        }," +
            "        {" +
            "            name : \"名前3\"," +
            "            age : \"年齢3\"" +
            "        }" +
            "    ]" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        testParser2();
    }

    private final String uri = "http://api.atnd.org/events/?keyword=android&format=json";

    private void testParser2() {
        AsyncJsonLoader asyncJsonLoader = new AsyncJsonLoader(new AsyncJsonLoader.AsyncCallback() {
            // 実行前
            public void preExecute() {
            }
            // 実行後
            public void postExecute(JSONObject result) {
                if (result == null) {
                    showLoadError(); // エラーメッセージを表示
                    return;
                }
                try {
                    // 各 ATND イベントのタイトルを配列へ格納
                    ArrayList<String> list = new ArrayList<>();
                    JSONArray eventArray = result.getJSONArray("events");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObj = eventArray.getJSONObject(i);
                        JSONObject event = eventObj.getJSONObject("event");
                        //Log.d("title", event.getString("title"));
                        list.add(event.getString("title"));
                    }

                    for (int i = 0; i < list.size(); i++) {
                        String title = list.get(i);
                        Log.d(TAG, "title[" + i + "] = " + title);
                    }
                    /*
                    // ListView 用のアダプタを作成
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                            getActivity(), android.R.layout.simple_list_item_1, list
                    );
                    // ListView にアダプタをセット
                    ListView listView = (ListView)getActivity().findViewById(R.id.listView);
                    listView.setAdapter(arrayAdapter);
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                    showLoadError(); // エラーメッセージを表示
                }
            }
            // 実行中
            public void progressUpdate(int progress) {
            }
            // キャンセル
            public void cancel() {
            }
        });
        // 処理を実行
        Log.d(TAG, uri);
        asyncJsonLoader.execute(uri);
    }

    // エラーメッセージ表示
    private void showLoadError() {
        Toast toast = Toast.makeText(this.getApplicationContext(), "データを取得できませんでした。", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void testParser() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            if (jsonObject != null) {
                JSONArray datas = jsonObject.getJSONArray("datas");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    String name = data.getString("name");
                    String age = data.getString("age");
                    Log.d(TAG, "name[" + i + "] = " + name);
                    Log.d(TAG, "age [" + i + "] = " + age);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException");
            e.printStackTrace();
        }
    }
}
