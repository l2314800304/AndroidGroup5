package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Repassword extends AppCompatActivity {
    Button btn_cancel,btn_ok;
    EditText et_Password;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(Repassword.this, "修改成功！返回详情页面...", Toast.LENGTH_LONG).show();
                    backToInsert();
                    break;
                case 1:
                    Toast.makeText(Repassword.this, "修改失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        init();
        String Password=((UserParameter)getApplication()).getUser().getPassword();
        et_Password.setText(Password);
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Repassword.this.finish();
            }
        });
    }

    private void init() {
        btn_cancel = (Button) findViewById(R.id.buttonCancel);
        btn_ok = (Button) findViewById(R.id.buttonOk);
        et_Password = (EditText) findViewById(R.id.buttonPassword);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(Repassword.this, "修改中...", Toast.LENGTH_LONG).show();
                    remark();
                } else {
                    Toast.makeText(Repassword.this, "修改失败，请检查修改信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void remark() {
        String UserName=((UserParameter) getApplication()).getUser().getUserName();
        String password = et_Password.getText().toString();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/ChangePassword.ashx?UserName="+ UserName+ "&Password=" + URLEncoder.encode(password))
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

    private void backToInsert(){
        startActivity(new Intent().setClass(Repassword.this, SkipActivity.class));
    }

    private boolean checkData() {
        String password = et_Password.getText().toString();
        if ( password.length() > 50 ) {
            return false;
        } else {
            return true;
        }
    }
}
