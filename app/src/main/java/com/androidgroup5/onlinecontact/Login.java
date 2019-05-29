package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private EditText usernameField;

    private EditText passwordField;

    private Button loginBtn;

    private int userId;

    private ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init(){
        usernameField = findViewById(R.id.login_et_user);
        passwordField = findViewById(R.id.login_et_pass);
        loginBtn = findViewById(R.id.login_btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = usernameField.getText().toString();
                String p = passwordField.getText().toString();
                if (u.isEmpty() || p.isEmpty()){
                    Toast.makeText(Login.this,"输入信息不合法",Toast.LENGTH_LONG).show();
                }else {
                    verifyLogin(u, p);
                }
            }
        });
        ((Button)findViewById(R.id.login_btn_toRegister)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Login.this,Register.class));
            }
        });
    }

    private void verifyLogin(final String username, final String password){



        OkHttpClient client = new OkHttpClient();
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("http://e24961611l.wicp.vip/login?UserName=" + URLEncoder.encode(username, "utf8") + "&Password=" + URLEncoder.encode(password))
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Login.this,"连接失败",Toast.LENGTH_LONG).show();
                }



                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response.isSuccessful()){

                        // 登录成功
                        Intent intent = new Intent();
                        intent.setClass(Login.this, ShowContactActivity.class);
                        String json = response.body().string();
                        Gson gson = new Gson();
                        Map<String, String> dataMap = gson.fromJson(json, new TypeToken< HashMap<String, String>>(){}.getType());

                        userId = Integer.parseInt(dataMap.get("ID"));

                        intent.putExtra("ID", dataMap.get("ID"));
                        intent.putExtra("UserName", dataMap.get("UserName"));
                        intent.putExtra("contacts", names);
                        sendReq(userId);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }else {
                        // 用户名或密码错误
                        Toast.makeText(Login.this,"服务器响应失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
        }

    }



    private void sendReq(int id){
        OkHttpClient client = new OkHttpClient();
        Request request = null;

        request = new Request.Builder()
                .url("http://e24961611l.wicp.vip/contacts?userId=" + id)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Login.this, "连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 加载个人联系人列表

                    Gson gson = new Gson();
                    String json = response.body().string();

                    Thread t = new MyThread();
                    ((MyThread) t).setJsonStr(json);
                    t.start();

                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


//                        List<String> data = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
//                        names.addAll(data);

                } else {
                    // 用户名或密码错误
                    Toast.makeText(Login.this, "数据加载失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class MyThread extends Thread{

        private String jsonStr;

        public String getJsonStr() {
            return jsonStr;
        }

        public void setJsonStr(String jsonStr) {
            this.jsonStr = jsonStr;
        }

        @Override
        public void run() {
//            Looper.prepare();

            Gson gson = new Gson();

            List<String> data = gson.fromJson(jsonStr, new TypeToken<List<String>>(){}.getType());
            names.addAll(data);

//            Toast.makeText(Login.this, Arrays.toString(names.toArray()),Toast.LENGTH_LONG).show();
//            Looper.loop();// 进入loop中的循环，查看消息队列
        }
    }

}
