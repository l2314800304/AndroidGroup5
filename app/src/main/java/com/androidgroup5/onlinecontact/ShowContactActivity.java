package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.contextDetail.ContactDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowContactActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private ArrayList<Contact> contactList = new ArrayList<>();

    private List<String> names = new ArrayList<>();

    private User user;

    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        init();
    }


    public void init(){

        listView = findViewById(R.id.listView);
        fetchData(16);
        adapter = new ArrayAdapter<String>(ShowContactActivity.this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }




    private void fetchData(int id){

        for (int i = 0; i < 100; i++){
//            Contact contact = new Contact();
//            contact.setID(i);
//            contact.setName("张三"+i);
//            contactList.add(new Contact());
            names.add("张三"+i);
        }

        Toast.makeText(ShowContactActivity.this,id+"-------",Toast.LENGTH_LONG).show();
//        URLEncoder.encode(user)
        // 根据id获取contactSet
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("http://192.168.43.199:8080/api/users/"+URLEncoder.encode(String.valueOf(id))+"/contactSet").method("GET",null).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()){
//                    // 加载个人联系人列表
//
//                    Toast.makeText(ShowContactActivity.this,response.code()+"-------",Toast.LENGTH_LONG).show();
//
////                    Gson gson = new Gson();
////                    contactList = gson.fromJson(response.body().string(),  new TypeToken<ArrayList<Contact>>(){}.getType());
////
////                    for (Contact i: contactList){
////                        names.add(i.getName());
////                    }
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
