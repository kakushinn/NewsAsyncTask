package com.example.guochen.newsasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.NewsAdapter;
import Entities.NewsBean;

        public class MainActivity extends AppCompatActivity {

            public static final String APIURL = "http://www.imooc.com/api/teacher?type=4&num=30";
            public ListView listView;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                listView = (ListView)findViewById(R.id.lv);
                new NewAsyncTask().execute(APIURL);
            }

            public List<NewsBean> getJsonData(String url){
                List<NewsBean> newsBeanList = new ArrayList<NewsBean>();
                try {
                    String jsonString = readStream(new URL(url).openStream());
                    JSONObject jsonObject;
                    NewsBean newsBean;
                    try {
                        jsonObject = new JSONObject(jsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0 ; i < jsonArray.length() ; i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            newsBean = new NewsBean();
                            newsBean.setIconUrl(jsonObject.getString("picSmall"));
                            newsBean.setTitle(jsonObject.getString("name"));
                            newsBean.setContent(jsonObject.getString("description"));
                            newsBeanList.add(newsBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("json",jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return newsBeanList;
            }

            public String readStream(InputStream is){
                String result = "";
                try {
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(is,"utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    try {
                        if((line=br.readLine()) != null){
                            result += line;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return result;
            }

            class NewAsyncTask extends  AsyncTask<String,Void, List<NewsBean>>{
                @Override
                protected List<NewsBean> doInBackground(String... params) {

                    return getJsonData(params[0]);
                }

                @Override
                protected void onPostExecute(List<NewsBean> newsBeans) {
                    super.onPostExecute(newsBeans);
                    NewsAdapter adapter = new NewsAdapter(MainActivity.this,newsBeans);
                    listView.setAdapter(adapter);
                }
            }
}
