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

import com.androidgroup5.onlinecontact.EntityClass.User;

/**
 * 时间选择器
 */
public class XinxiActivity extends AppCompatActivity {
    Button btn;
    private TextView Username,Sex,Location,Remark;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinxi_layout);
        Button btn9=findViewById(R.id.btntuichu);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it9=new Intent();
                it9.setClass(XinxiActivity.this,SkipActivity.class);
                startActivity(it9);
            }
        });
        Username=(TextView)findViewById(R.id.txt_username);
        Sex=(TextView)findViewById(R.id.txt_sex);
        Location=(TextView)findViewById(R.id.txt_location);
        Remark=(TextView)findViewById(R.id.txt_remark);
        User u=((UserParameter)getApplication()).getUser();
        Username.setText("用户名："+u.getUserName());
        Sex.setText("性  别："+u.getSex());
        Location.setText("位  置："+u.getLocation());
        Remark.setText("备  注："+u.getRemark());
        Button btn3=findViewById(R.id.btn6);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it3=new Intent();
                it3.setClass(XinxiActivity.this,Remark.class);
                startActivity(it3);

            }
        });

    }
}
