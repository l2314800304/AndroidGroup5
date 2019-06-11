package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

public class Remark extends AppCompatActivity {
    Button btn_cancel,btn_ok;
    EditText et_Remark,et_Location;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(Remark.this, "修改成功！返回详情页面...", Toast.LENGTH_LONG).show();
                    ((UserParameter)getApplication()).getUser().setRemark(et_Remark.getText().toString());
                    ((UserParameter)getApplication()).getUser().setSex(((boolean)((RadioButton)findViewById(R.id.change_rbtn_man)).isChecked())?"男":"女");
                    ((UserParameter)getApplication()).getUser().setLocation(et_Location.getText().toString());
                    backToInsert();
                    break;
                case 1:
                    Toast.makeText(Remark.this, "修改失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        init();
        String Remark=((UserParameter)getApplication()).getUser().getRemark();
        et_Remark.setText(Remark);
        et_Location.setText(((UserParameter)getApplication()).getUser().getLocation());
        RadioButton man=(RadioButton)findViewById(R.id.change_rbtn_man),woman=(RadioButton)findViewById(R.id.change_rbtn_woman);
        if(((UserParameter)getApplication()).getUser().getSex().equals("男")){
            man.setSelected(true);
            woman.setSelected(false);
        }else{
            man.setSelected(false);
            woman.setSelected(true);
        }
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Remark.this.finish();
            }
        });
    }

    private void init() {
        btn_cancel = (Button) findViewById(R.id.buttonCancel);
        btn_ok = (Button) findViewById(R.id.buttonOk);
        et_Remark = (EditText) findViewById(R.id.change_et_Remark);
        et_Location=(EditText)findViewById(R.id.change_et_location);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(Remark.this, "修改中...", Toast.LENGTH_LONG).show();
                    remark();
                } else {
                    Toast.makeText(Remark.this, "修改失败，请检查修改信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void remark() {
        String UserName=((UserParameter) getApplication()).getUser().getUserName();
        String remark = et_Remark.getText().toString();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        ((UserParameter) getApplication()).getUser().setRemark(remark);
        ((UserParameter) getApplication()).getUser().setLocation(((EditText)findViewById(R.id.change_et_location)).getText().toString());
        ((UserParameter) getApplication()).getUser().setSex(((boolean)((RadioButton)findViewById(R.id.change_rbtn_man)).isChecked())?"男":"女");
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/ChangeRemark.ashx?UserName="+ UserName+ "&Sex="+(((boolean)((RadioButton)findViewById(R.id.change_rbtn_man)).isChecked())?"男":"女")+"&Location="+(((EditText)findViewById(R.id.change_et_location)).getText().toString())+"&Remark=" + URLEncoder.encode(remark))
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
       startActivity(new Intent().setClass(Remark.this, XinxiActivity.class));
     }

    private boolean checkData() {
        String
                remark = et_Remark.getText().toString();
        if ( remark.length() > 50 ) {
            return false;
        } else {
            return true;
        }
    }
}
