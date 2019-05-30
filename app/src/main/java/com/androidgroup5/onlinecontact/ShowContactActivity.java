package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
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

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        Intent intent = getIntent();
        userName =  intent.getStringExtra("UserName");
        userId = Integer.parseInt(intent.getStringExtra("ID"));
        names = intent.getStringArrayListExtra("contacts");
        initData();
    }


    private void initData(){

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(ShowContactActivity.this);
        adapter = new ArrayAdapter(ShowContactActivity.this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ListView listView = (ListView)parent;
        String data = (String) listView.getItemAtPosition(position);

        Toast.makeText(ShowContactActivity.this,"技术君努力开发中"+data,Toast.LENGTH_LONG).show();
        //定义Toast消息
    }
}
