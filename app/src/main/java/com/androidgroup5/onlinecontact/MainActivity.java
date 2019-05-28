package com.androidgroup5.onlinecontact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.QRCode.QRCodeActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button btn01,btn02,
            btn11,btn12,
            btn21,btn22,
            btn31,btn32,
            btn41,btn42,
            btn51,btn52,
            btn61,btn62,
            btn71,btn72,
            btn81,btn82,
            btn91,btn92;
    User user=null;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    UserParameter p = (UserParameter) getApplication();
                    p.setUser(user);
                    Toast.makeText(MainActivity.this,"数据加载成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    public void GetAllInfo(String UserName) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("UserName", UserName);
        paramsMap.put("Contact", "[]");
        paramsMap.put("Record", "[]");
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/SyncAllContactByUserName.ashx")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    user = gson.fromJson(json, User.class);
                    Message message = new Message();
                    message.what = 10;
                    handler.sendMessage(message);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        GetAllInfo("宋甜乐");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void init(){
        btn01=(Button)findViewById(R.id.btn_01);
        btn01.setOnClickListener(new mClick(1));
        btn02=(Button)findViewById(R.id.btn_02);
        btn02.setOnClickListener(new mClick(2));
        btn11=(Button)findViewById(R.id.btn_11);
        btn11.setOnClickListener(new mClick(11));
        btn12=(Button)findViewById(R.id.btn_12);
        btn12.setOnClickListener(new mClick(12));
        btn21=(Button)findViewById(R.id.btn_21);
        btn21.setOnClickListener(new mClick(21));
        btn22=(Button)findViewById(R.id.btn_22);
        btn22.setOnClickListener(new mClick(22));
        btn31=(Button)findViewById(R.id.btn_31);
        btn31.setOnClickListener(new mClick(31));
        btn32=(Button)findViewById(R.id.btn_32);
        btn32.setOnClickListener(new mClick(32));
        btn41=(Button)findViewById(R.id.btn_41);
        btn41.setOnClickListener(new mClick(41));
        btn42=(Button)findViewById(R.id.btn_42);
        btn42.setOnClickListener(new mClick(42));
        btn51=(Button)findViewById(R.id.btn_51);
        btn51.setOnClickListener(new mClick(51));
        btn52=(Button)findViewById(R.id.btn_52);
        btn52.setOnClickListener(new mClick(52));
        btn61=(Button)findViewById(R.id.btn_61);
        btn61.setOnClickListener(new mClick(61));
        btn62=(Button)findViewById(R.id.btn_62);
        btn62.setOnClickListener(new mClick(62));
        btn71=(Button)findViewById(R.id.btn_71);
        btn71.setOnClickListener(new mClick(71));
        btn72=(Button)findViewById(R.id.btn_72);
        btn72.setOnClickListener(new mClick(72));
        btn81=(Button)findViewById(R.id.btn_81);
        btn81.setOnClickListener(new mClick(81));
        btn82=(Button)findViewById(R.id.btn_82);
        btn82.setOnClickListener(new mClick(82));
        btn91=(Button)findViewById(R.id.btn_91);
        btn91.setOnClickListener(new mClick(91));
        btn92=(Button)findViewById(R.id.btn_92);
        btn92.setOnClickListener(new mClick(92));
    }
    class mClick implements View.OnClickListener{
        int _id;
        public mClick(int id){
            _id=id;
        }

        @Override
        public void onClick(View v) {
            switch (_id){
                case 1:
                    startActivity(new Intent().setClass(MainActivity.this,Register.class));
                    break;
                case 2:
                    startActivity(new Intent().setClass(MainActivity.this,SyncAddressBook.class).putExtra("UserName","宋甜乐"));
                    break;
                case 11:
                    startActivity(new Intent().setClass(MainActivity.this,SkipActivity.class));
                    break;
                case 12:
                    break;
                case 21:
                    startActivity(new Intent().setClass(MainActivity.this,Delete.class));
                    break;
                case 22:
                    startActivity(new Intent().setClass(MainActivity.this,Remark.class));
                    break;
                case 31:
                    startActivity(new Intent().setClass(MainActivity.this,Login.class));
                    break;
                case 32:
                    startActivity(new Intent().setClass(MainActivity.this, ShowContactActivity.class));
                    break;
                case 41://通讯录详情页
                    startActivity(new Intent().setClass(MainActivity.this, ContactDetailActivity.class).putExtra("rawContactId",5364));
                    break;
                case 42://通话记录
                    startActivity(new Intent().setClass(MainActivity.this, CallLogActivity.class));
                    break;
                case 51:
                    startActivity(new Intent().setClass(MainActivity.this,Insert.class));
                    break;
                case 52:
                    break;
                case 61:
                    break;
                case 62:
                    break;
                case 71:
                    startActivity(new Intent().setClass(MainActivity.this,Phone.class));
                    break;
                case 72:
                    startActivity(new Intent().setClass(MainActivity.this,Birthday.class));
                    break;
                case 81:
                    startActivity(new Intent().setClass(MainActivity.this,QRCodeActivity.class).putExtra("ContactName1","张三")
                    .putExtra("ContactNumber","31321231"));
                    break;
                case 82:
                    startActivity(new Intent().setClass(MainActivity.this,EditContactDetailActivity.class).putExtra("ContactName","张三"));
                    break;
                case 91:
                    startActivity(new Intent().setClass(MainActivity.this,Find.class).putExtra("UserName","宋甜乐"));
                    break;
                case 92:
                    startActivity(new Intent().setClass(MainActivity.this,Export.class));
                    break;
            }
        }
    }
}
