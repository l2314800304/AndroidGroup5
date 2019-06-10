package com.androidgroup5.onlinecontact;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.Contact;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateBirthday extends AppCompatActivity {

    int mYear, mMonth, mDay;
    String path="";
    ArrayAdapter<String> adapter;
    Spinner spinner;
    Button date,update,back;
    List<Contact> contacts;
    Contact con;
    final int DATE_DIALOG = 1; private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(UpdateBirthday.this, "修改成功，返回登录页面...", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(UpdateBirthday.this, "修改失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_birthday);
        contacts=((UserParameter)getApplication()).getUser().getContact();
        spinner=(Spinner)findViewById(R.id.spinner_contact);
        date=(Button)findViewById(R.id.btn_date);
        update=(Button)findViewById(R.id.btn_ub);
        back=(Button)findViewById(R.id.btn_bb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(UpdateBirthday.this,Birthday.class));
            }
        });
        List<String> contact=new ArrayList<>();
        for(int i=0;i<contacts.size();i++){
            contact.add(contacts.get(i).getName());
        }
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,contact);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                con=contacts.get(position);
                date.setText(con.getBirthday().isEmpty()?"选择日期":con.getBirthday());
                if(!con.getBirthday().isEmpty()){
                    mYear=new Integer(con.getBirthday().split("-")[0]);
                    mMonth=new Integer(con.getBirthday().split("-")[1]);
                    mDay=new Integer(con.getBirthday().split("-")[2]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS).build();
                Request request = new Request.Builder()
                        .url("http://114.116.171.181/SetBirthday.ashx?Contact_ID="+con.getID()+"&Birthday="+date.getText().toString())
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
                                con.setBirthday(date.getText().toString());
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
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                if(mYear==0)
                    return new DatePickerDialog(this, mdateListener, 1995, 0, 0);
                else
                    return new DatePickerDialog(this,mdateListener,mYear,mMonth,mDay);
        }
        return null;
    }
    public void display() {
        date.setText(new StringBuffer().append(mYear).append("-").append((mMonth<9?"0"+(mMonth + 1):""+(mMonth + 1))).append("-").append((mDay<10?"0"+mDay:""+mDay)));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
}
