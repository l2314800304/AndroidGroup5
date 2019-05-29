package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Updatemm extends AppCompatActivity {
    Button btn_cancel,btn_ok;
    EditText et_Pass;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(Updatemm.this, "修改成功！返回详情页面...", Toast.LENGTH_LONG).show();
                    //backToInsert();
                    break;
                case 1:
                    Toast.makeText(Updatemm.this, "修改失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatemm);
        init();
    }

    private void init() {
        btn_cancel = (Button) findViewById(R.id.buttonCancel);
        btn_ok = (Button) findViewById(R.id.buttonOk);
        et_Pass = (EditText) findViewById(R.id.buttonPass);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(Updatemm.this, "修改中...", Toast.LENGTH_LONG).show();
                    register();
                } else {
                    Toast.makeText(Updatemm.this, "修改失败，请检查修改信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void register() {
        String pass = et_Pass.getText().toString();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/UpdateContactInfo.ashx?contact_info_ID=303830" + "&Pass" + URLEncoder.encode(pass))
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
                if(response.isSuccessful()){
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

    //private void backToInsert(){
    //   startActivity(new Intent().setClass(Pass.this, Insert.class));
    // }

    private boolean checkData() {
        String pass = et_Pass.getText().toString();
        if ( pass.length() > 50 ) {
            return false;
        } else {
            return true;
        }
    }
}
