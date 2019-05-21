package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

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
        usernameField = findViewById(R.id.editText7);
        passwordField = findViewById(R.id.editText9);
        loginBtn = findViewById(R.id.button4);
        loginBtn.setOnClickListener(e->{

            String u = usernameField.getText().toString();
            String p = passwordField.getText().toString();
            if (u.isEmpty() || p.isEmpty()){
                Toast.makeText(Login.this,"输入信息不合法",Toast.LENGTH_LONG).show();
            }else {

                verifyLogin(u, p);
            }
        });
    }

    private void verifyLogin(String username, String password){


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.43.199:8080/api/users/search/findUserByUsernameAndPassword?username="+ URLEncoder.encode(username)+"&password="+URLEncoder.encode(password)).method("GET",null).build();

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
//                    Gson gson = new Gson();
//                    User user = gson.fromJson(response.body().string(),new TypeToken<User>(){}.getType());
//                    Toast.makeText(Login.this,user.getID(),Toast.LENGTH_LONG).show();
//                    intent.putExtra("user", user);
                    startActivity(intent);
                }else {
                    // 用户名或密码错误
                    Toast.makeText(Login.this,"登陆失败,用户名或密码错误",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

}
