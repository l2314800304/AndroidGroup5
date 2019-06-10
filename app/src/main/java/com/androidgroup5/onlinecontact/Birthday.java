package com.androidgroup5.onlinecontact;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.EntityClass.User;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Birthday extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        TextView textView1=(TextView)findViewById(R.id.text1);
        TextView textView2=(TextView)findViewById(R.id.text2);
        TextView textView3=(TextView)findViewById(R.id.text3);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        //ParsePosition pos = new ParsePosition(8);
        Date currentTime = new Date();
        String dateString1 = formatter.format(currentTime);
        ((Button)findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Birthday.this,SkipActivity.class));
            }
        });
        textView2.setText("今天日期是"+dateString1 );
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<((UserParameter) getApplication()).getUser().getContact().size();i++)
        {
            String date2 = ((UserParameter) getApplication()).getUser().getContact().get(i).getBirthday();
            if (date2 != null && date2.length() != 0)
            {
                String date1=date2.substring(5,10);
                if (dateString1.equals(date1))
                {
                    textView3.setText("今天是好友的生日，打个电话送祝福吧！");
                    textView1.setText(sb.append(((UserParameter) getApplication()).getUser().getContact().get(i).getName()+"   "));
                }
                else
                {
                    textView3.setText("很抱歉，今天不是好友生日，请明天查看！");
                }
             }
         }
    }
}
