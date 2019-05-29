package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.EntityClass.User;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Birthday extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        TextView textView1=(TextView)findViewById(R.id.text1);
        TextView textView2=(TextView)findViewById(R.id.text2);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        //ParsePosition pos = new ParsePosition(8);
        Date currentTime = new Date();
        String dateString1 = formatter.format(currentTime);
        textView1.setText(dateString1 );
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<((UserParameter) getApplication()).getUser().getContact().size();i++)
        {
            String date2 = ((UserParameter) getApplication()).getUser().getContact().get(i).getBirthday();
            if (date2 != null && date2.length() != 0)
            {
                String date1=date2.substring(5,10);
                textView2.setText(date1);
                if (dateString1.equals(date1))
                {
                    textView1.setText(sb.append(((UserParameter) getApplication()).getUser().getContact().get(i).getName()+"   "));
                    //((UserParameter) getApplication()).getUser().getContact().get(i).getName());

                }
             }
         }
    }
}
