package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;

public class Insert extends AppCompatActivity {
    //通用的显示对话框的方法
    private void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示的标题
        builder.setTitle(title);
        //设置对话框现实的信息
        builder.setMessage(msg);
        //设置对话框的按钮
        builder.setPositiveButton("确定", null);
        //显示对话框
        builder.create().show();
        Intent intent;
    }
    //两个按钮共用一个单击事件方法，通过按钮的id区分单击了哪个按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
    }
}
