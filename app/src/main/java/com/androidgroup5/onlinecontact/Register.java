package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    Button btn_backToLogin, btn_register;
    EditText et_user, et_pass, et_pass1, et_remark, et_location;
    RadioButton rbtn_man;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(Register.this, "注册成功，返回登录页面...", Toast.LENGTH_LONG).show();
                    backToLogin();
                    break;
                case 1:
                    Toast.makeText(Register.this, "注册失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    private void register() {
        String user = et_user.getText().toString(),
                pass = et_pass.getText().toString(),
                sex = rbtn_man.isChecked() ? "男" : "女",
                location = et_location.getText().toString(),
                remark = et_remark.getText().toString();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/Register.ashx?UserName=" + URLEncoder.encode(user) + "&Password=" + pass + "&Sex=" + URLEncoder.encode(sex) + "&Location=" + URLEncoder.encode(location) + "&Remark=" + URLEncoder.encode(remark))
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    if (response.body().string().equals("OK")) {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        btn_backToLogin = (Button) findViewById(R.id.btn_backToLogin);
        btn_register = (Button) findViewById(R.id.btn_register);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass1 = (EditText) findViewById(R.id.et_pass1);
        et_remark = (EditText) findViewById(R.id.et_remark);
        et_location = (EditText) findViewById(R.id.et_location);
        rbtn_man = (RadioButton) findViewById(R.id.rbtn_man);
        btn_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(Register.this, "注册中...", Toast.LENGTH_LONG).show();
                    register();
                } else {
                    Toast.makeText(Register.this, "注册失败，请检查注册信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void backToLogin() {
        startActivity(new Intent().setClass(Register.this, Login.class));
    }

    private boolean checkData() {
        String user = et_user.getText().toString(),
                pass = et_pass.getText().toString(),
                pass1 = et_pass1.getText().toString(),
                location = et_location.getText().toString(),
                remark = et_remark.getText().toString();
        if (user.length() < 4 || user.length() > 8 || pass.length() < 6 || pass.length() > 16 || !pass.equals(pass1) || remark.length() > 50 || location.length() > 20) {
            return false;
        } else {
            return true;
        }
    }
}
