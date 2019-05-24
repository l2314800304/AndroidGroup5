package com.androidgroup5.onlinecontact;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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
    private String UserName = "";
    TextView state;
    User u = new User();
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String m = "";
            switch (msg.what) {
                case 10:
                    m = "正在提取本地通讯录信息，由于数据较多，请耐心等待...";
                    break;
                case 11:
                    m = "提取成功，正在同步通讯录信息以及通话信息，由于数据较多，请耐心等待...";
                    break;
                case 14:
                    m = "恭喜，同步成功！";
                    break;
                case -1:
                    m = "抱歉，同步失败，请检查网络连接！";
                default:
                    break;
            }
            state.setText(m);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_address_book);
        UserName = this.getIntent().getExtras().getString("UserName");
        state = (TextView) findViewById(R.id.txt_state);
        ((Button) findViewById(R.id.btn_sync)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });
    }

    private void Update() {
        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("UserName", UserName);
        paramsMap.put("Contact", (new Gson().toJson(GetContactFromLocal())));
        paramsMap.put("Record", (new Gson().toJson(GetRecordFromLocal())));
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS).build();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/SyncAllContactByUserName.ashx")
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
                if (response.isSuccessful()) {
                    Message message1 = new Message();
                    message1.what = 11;
                    handler.sendMessage(message1);
                    String json = response.body().string();
                    Gson gson = new Gson();
                    u = gson.fromJson(json, User.class);
                    List<Contact> contact = u.getContact();
                    if(contact.size()>0)
                    syncTSContactsToContactsProvider(contact);
                    List<Record> record = u.getRecord();
                    try {
                        if(record.size()>0)
                        BatchAddCallLogs(record);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 14;
                    handler.sendMessage(message);
                } else {
                    Log.i("Response:", response.body().string());
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            }
        });
    }
    public void BatchAddCallLogs(List<Record> list)
            throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentValues values = new ContentValues();
        int count=0;
        for (Record r : list) {
            values.clear();
            values.put(CallLog.Calls.NUMBER, r.getNumber());
            values.put(CallLog.Calls.TYPE, r.getType());
            values.put(CallLog.Calls.DATE, r.getDate());
            values.put(CallLog.Calls.DURATION, r.getDuration());
            values.put(CallLog.Calls.NEW, "0");// 0已看1未看 ,由于没有获取默认全为已读
            ops.add(ContentProviderOperation
                    .newInsert(CallLog.Calls.CONTENT_URI).withValues(values)
                    .withYieldAllowed(true).build());
            count++;
            if(count==400){
                count=0;
                if (ops != null) {
                    ContentProviderResult[] results = getContentResolver()
                            .applyBatch(CallLog.AUTHORITY, ops);
                }
                ops.clear();
            }
            state.setText("正在添加通话记录："+ops.size()+"//"+list.size());
        }
        if (ops != null) {
            ContentProviderResult[] results = getContentResolver()
                    .applyBatch(CallLog.AUTHORITY, ops);
        }
    }
    private void syncTSContactsToContactsProvider(List<Contact> contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (int index = 0; index < contact.size(); index++) {
            Contact con = contact.get(index);
            int rawContactInsertIndex = ops.size();
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, con.getName()).withYieldAllowed(true).build());
            for(int i=0;i<con.getContactInfos().size();i++)
                if(con.getContactInfos().get(i).getEmailOrNumber()==5)
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, con.getContactInfos().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContactInfos().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
                else
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, con.getContactInfos().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContactInfos().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            state.setText("正在添加联系人："+index+"//"+contact.size());
        }
    }
    private List<Contact> GetContactFromLocal() {
        List<Contact> contacts = new ArrayList<Contact>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = SyncAddressBook.this.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            List<ContactInfos> contactInfos = new ArrayList<ContactInfos>();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);
            while (phones.moveToNext()) {
                ContactInfos info = new ContactInfos();
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                //添加Phone的信息
                String phoneType = phones.getString(phones.getColumnIndex(
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
            while (emails.moveToNext()) {
                ContactInfos info = new ContactInfos();
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

    private List<Record> GetRecordFromLocal() {
        List<Record> records = new ArrayList<Record>();
        ContentResolver contentResolver = SyncAddressBook.this.getContentResolver();
        Cursor recor = contentResolver.query(CallLog.Calls.CONTENT_URI,
                null,
                null,
                null, null);
        while (recor.moveToNext()) {
            Record info = new Record();
            int type = recor.getInt(recor.getColumnIndex(CallLog.Calls.TYPE));
            String number = recor.getString(recor.getColumnIndex(
                    CallLog.Calls.NUMBER));
            String duration = recor.getString(recor.getColumnIndex(
                    CallLog.Calls.DURATION));
            String date = recor.getString(recor.getColumnIndex(
                    CallLog.Calls.DATE));
            info.setNumber(number);
            info.setDuration(duration);
            info.setDate(date);
            info.setType(type + "");
            records.add(info);
        }
        recor.close();
        return records;
    }
}
