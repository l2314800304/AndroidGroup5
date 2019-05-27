package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowContactActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private ArrayList<HashMap<String, Object>> contactList = new ArrayList<>();

    private ArrayList<String> names = new ArrayList<>();

    private int userId;

    private String userName;

    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        Intent intent = getIntent();
        userName =  intent.getStringExtra("UserName");
        userId = Integer.parseInt(intent.getStringExtra("ID"));
        init();
    }


    public void init(){

        listView = findViewById(R.id.listView);
        fetchData(userId);
        adapter = new ArrayAdapter<String>(ShowContactActivity.this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(ShowContactActivity.this);

    }




    private void fetchData(int id){

        names.add(userName);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .build();
//        Request request = new Request.Builder().url("http://wuwensen007.ticp.io/contacts?userId=" + id).get().build();
//
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Toast.makeText(ShowContactActivity.this,"连接失败",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()){
//                    // 加载个人联系人列表
//
//                    Gson gson = new Gson();
//                    String json =  response.body().string();
//
////                    Toast.makeText(ShowContactActivity.this,json,Toast.LENGTH_LONG).show();
//                    List<String> data = gson.fromJson(json, new TypeToken< ArrayList<String>>(){}.getType());
//                    names.addAll(data);
//
//                }else {
//                    // 用户名或密码错误
//                    Toast.makeText(ShowContactActivity.this,"数据加载失败",Toast.LENGTH_LONG).show();
//                }
//            }
//        });



    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListView listView = (ListView)parent;
        String data = (String) listView.getItemAtPosition(position);

        Toast.makeText(ShowContactActivity.this,"技术君努力开发中"+data,Toast.LENGTH_LONG).show();
        //定义Toast消息
    }
}
