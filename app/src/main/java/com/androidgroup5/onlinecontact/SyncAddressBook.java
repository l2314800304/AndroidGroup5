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
                    m = "正在下载云端通讯录信息，由于数据较多，请耐心等待...";
                    break;
                case 11:
                    m = "下载成功，正在同步本地通讯录信息以及通话信息，由于数据较多，请耐心等待...";
                    break;
                case 12:
                    m = "正在提取本地通讯录信息，由于数据较多，请耐心等待...";
                    break;
                case 13:
                    m = "提取成功，正在同步云端通讯录信息以及通话信息，由于数据较多，请耐心等待...";
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
                UpdateLocal();
            }
        });
    }

    private void UpdateLocal() {
        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/GetContactByUserName.ashx?UserName=" + UserName)
                .method("GET", null)
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
                    String json = response.body().string();
                    Gson gson = new Gson();
                    u = gson.fromJson(json, User.class);
                    Message message = new Message();
                    message.what = 11;
                    handler.sendMessage(message);
                    List<Contact> contact = u.getContact();
                    for (int i = 0; i < contact.size(); i++) {
                        deleteContactPhoneNumber(contact.get(i).getName());
                    }
                    syncTSContactsToContactsProvider(contact);
                    List<Record> record = u.getRecord();
                    try {
                        BatchAddCallLogs(record);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    UpdateCloud();
                } else {
                    Log.i("Response:", response.body().string());
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void insertCallLog(Record record) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, record.getNumber());
        values.put(CallLog.Calls.DATE, record.getDate());
        values.put(CallLog.Calls.DURATION, record.getDuration());
        values.put(CallLog.Calls.TYPE, record.getType());
        values.put(CallLog.Calls.NEW, 0);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALL_LOG}, 1000);
        }
        getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
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
        }
        if (ops != null) {
            ContentProviderResult[] results = getContentResolver()
                    .applyBatch(CallLog.AUTHORITY, ops);
        }
    }
    private void deleteCallLog() {
        ContentResolver resolver = getContentResolver();
        int result = resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

    private void syncTSContactsToContactsProvider(List<Contact> contact) {
        final int contactsListSize = contact.size();
        int unitLength = 400;
        int syncedCount = 0;
        while (syncedCount < contactsListSize) {
            int syncLength = (contactsListSize - syncedCount) < unitLength ? (contactsListSize - syncedCount) : unitLength;
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            for (int index = 0; index < contact.size(); index++) {
                Contact con = contact.get(index);
                int rawContactInsertIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withYieldAllowed(true).build());
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, con.getName()).withYieldAllowed(true).build());
                for(int i=0;i<con.getContact_Info().size();i++)
                    if(con.getContact_Info().get(i).getEmailOrNumber()==5)
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, con.getContact_Info().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContact_Info().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
                    else
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, con.getContact_Info().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContact_Info().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
                syncedCount++;
            }
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteContactPhoneNumber(String contactName) {
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        String where = ContactsContract.PhoneLookup.DISPLAY_NAME;
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, where + "=?", new String[]{contactName}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, where + "=?", new String[]{contactName});
            uri = ContactsContract.Data.CONTENT_URI;
            resolver.delete(uri, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{id + ""});
        }
    }

    private void UpdateCloud() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("UserName", UserName);
        Message message = new Message();
        message.what = 12;
        handler.sendMessage(message);
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
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    if (response.body().string().equals("OK")) {
                        Message message = new Message();
                        message.what = 14;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = -1;
                        handler.sendMessage(message);
                    }
                } else {
                    Log.i("Response:", response.body().string());
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            }
        });
        message = new Message();
        message.what = 13;
        handler.sendMessage(message);
    }

    private List<Contact> GetContactFromLocal() {
        List<Contact> contacts = new ArrayList<Contact>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = SyncAddressBook.this.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);
            while (phones.moveToNext()) {
                ContactInfo info = new ContactInfo();
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
                ContactInfo info = new ContactInfo();
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
            contact.setContact_Info(contactInfos);
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
