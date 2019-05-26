package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
public class MyTwoActivity extends AppCompatActivity implements View.OnClickListener{
    protected   void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytwo);
        //初始化
        initUI();
    }


    private void initUI() {
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                //跳转到第一个界面
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MyOneActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                break;
        }
    }


}
