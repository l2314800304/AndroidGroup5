package com.androidgroup5.onlinecontact;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 时间选择器
 */
public class XinxiActivity extends AppCompatActivity {
    Button btn;
    private EditText Password;
    private EditText Username;
    private EditText Sex;
    private EditText Location;
    private EditText Remark;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinxi_layout);
       /* init();
        String Remark=((UserParameter)getApplication()).getUser().getRemark();
        buttonRemark.setText(Remark);
        String Username=((UserParameter)getApplication()).getUser().getUserName();
        buttonUsername.setText(Remark);
        String Sex=((UserParameter)getApplication()).getUser().getSex();
        buttonSex.setText(Remark);
        String Location=((UserParameter)getApplication()).getUser().getLocation();
        buttonLocation.setText(Remark);
        String Password=((UserParameter)getApplication()).getUser().getPassword();
        buttonPassword.setText(Remark);*/

        Button btn9=findViewById(R.id.btntuichu);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it9=new Intent();
                it9.setClass(XinxiActivity.this,SkipActivity.class);
                XinxiActivity.this.startActivity(it9);

            }
        });
        /*private void init() {
            Username = (EditText)findViewById(R.id.buttonUsername);
            Sex = (EditText) findViewById(R.id.buttonSex);
            Location = (EditText) findViewById(R.id.buttonLocation);
            Password = (EditText) findViewById(R.id.buttonPassword);
            Remark= (EditText) findViewById(R.id.buttonRemark);

        }*/


    }



}
