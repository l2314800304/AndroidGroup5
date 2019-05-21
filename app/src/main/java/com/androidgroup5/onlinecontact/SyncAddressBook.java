package com.androidgroup5.onlinecontact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.*;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SyncAddressBook extends AppCompatActivity {
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(SyncAddressBook.this, "同步云端通讯录成功", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(SyncAddressBook.this, "同步本地通讯录成功", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(SyncAddressBook.this, "同步失败，请检查网络连接！", Toast.LENGTH_LONG).show();
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_address_book);
        ((Button)findViewById(R.id.btn_sync)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)findViewById(R.id.ckb_local)).isChecked()){
                    UpdateLocal();
                }
                if(((CheckBox)findViewById(R.id.ckb_cloud)).isChecked()){
                    UpdateCloud();
                }
            }
        });
    }
    private boolean UpdateLocal(){
        return true;
    }
    private boolean UpdateCloud(){
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("UserName","宋甜乐");
        paramsMap.put("Contact",(new Gson().toJson(GetContactFromLocal())));
        paramsMap.put("Record",(new Gson().toJson(GetRecordFromLocal())));
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody body=builder.build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/SetContactByUserName.ashx")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = -1;
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
                        message.what = -1;
                        handler.sendMessage(message);
                    }
                }else{
                    Log.i("Response:",response.body().string());
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            }
        });
        return true;
    }

    private List<Contact> GetContactFromLocal(){
        List<Contact> contacts=new ArrayList<Contact>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = SyncAddressBook.this.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while(cursor.moveToNext())
        {
            Contact contact=new Contact();
            List<ContactInfo> contactInfos=new ArrayList<ContactInfo>();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                    null, null);
            while(phones.moveToNext())
            {
                ContactInfo info=new ContactInfo();
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                //添加Phone的信息
                String phoneType=phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.TYPE));
                info.setEmailOrNumber(5);
                info.setNumber(phoneNumber);
                info.setType(phoneType);
                contactInfos.add(info);
            }
            phones.close();
            Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                    null, null);
            while (emails.moveToNext())
            {
                ContactInfo info=new ContactInfo();
                String emailAddress = emails.getString(emails.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.DATA));
                String emailType = emails.getString(emails.getColumnIndex(
                        ContactsContract.CommonDataKinds.Email.TYPE));
                info.setEmailOrNumber(1);
                info.setNumber(emailAddress);
                info.setType(emailType);
                contactInfos.add(info);
            }
            emails.close();
            contact.setContactInfos(contactInfos);
            contacts.add(contact);
        }

        cursor.close();
        return contacts;
    }
    private List<Record> GetRecordFromLocal(){
        List<Record> records=new ArrayList<Record>();
        ContentResolver contentResolver = SyncAddressBook.this.getContentResolver();
        Cursor recor = contentResolver.query(CallLog.Calls.CONTENT_URI,
                null,
                null,
                null, null);
        while (recor.moveToNext())
        {
            Record info=new Record();
            int type=recor.getInt(recor.getColumnIndex(CallLog.Calls.TYPE));
            String number = recor.getString(recor.getColumnIndex(
                    CallLog.Calls.NUMBER));
            String duration = recor.getString(recor.getColumnIndex(
                    CallLog.Calls.DURATION));
            String date=recor.getString(recor.getColumnIndex(
                    CallLog.Calls.DATE));
            info.setNumber(number);
            info.setDuration(duration);
            info.setDate(date);
            info.setType(type);
            records.add(info);
        }
        recor.close();
        return records;
    }
}
