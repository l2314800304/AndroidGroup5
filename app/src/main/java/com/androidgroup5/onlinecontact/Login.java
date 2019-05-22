package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private EditText usernameField;

    private EditText passwordField;

    private Button loginBtn;

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

    private void verifyLogin(String username, String password){


        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/Login.ashx?UserName=" + URLEncoder.encode(username) + "&Password=" + URLEncoder.encode(password))
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Login.this,"连接失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    Toast.makeText(Login.this,"连接成功",Toast.LENGTH_LONG).show();
                    // 登录成功
                    Intent intent = new Intent();
                    intent.setClass(Login.this, ShowContactActivity.class);
                    startActivity(intent);
                }else {
                    // 用户名或密码错误
                    Toast.makeText(Login.this,"服务器响应失败",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

}
