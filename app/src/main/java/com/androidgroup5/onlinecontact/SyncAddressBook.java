package com.androidgroup5.onlinecontact;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
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

public class SyncAddressBook extends Activity {
    private String UserName = "";
    TextView state;
    User u = new User();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    startActivity(new Intent().setClass(SyncAddressBook.this,Find.class));
                    return true;
                case R.id.navigation_record:
                    startActivity(new Intent().setClass(SyncAddressBook.this,CallLogActivity.class));
                    return true;
                case R.id.navigation_sync:
                    return true;
                case R.id.navigation_call:
                    startActivity(new Intent().setClass(SyncAddressBook.this,Phone.class));
                    return true;
                case R.id.navigation_mine:
                    startActivity(new Intent().setClass(SyncAddressBook.this,SkipActivity.class));
                    return true;
            }
            return false;
        }
    };
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
                    m="正在添加联系人："+(int)(msg.what-100)+"/"+u.getContact().size();
                    break;
            }
            state.setText(m);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_address_book);
        UserName = ((User)((UserParameter)getApplication()).getUser()).getUserName();
        state = (TextView) findViewById(R.id.txt_state);
        ((Button) findViewById(R.id.btn_sync)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(2).getItemId());
    }

    private void Update() {
        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("UserName", UserName);
        User u1=((UserParameter) getApplication()).getLocal();
        paramsMap.put("Contact", (new Gson().toJson(u1.getContact())));
        paramsMap.put("Record", (new Gson().toJson(u1.getRecord())));
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
                    ((UserParameter)getApplication()).setUser(u);
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
        }
        if (ops != null) {
            ContentProviderResult[] results = getContentResolver()
                    .applyBatch(CallLog.AUTHORITY, ops);
        }
    }
    private void syncTSContactsToContactsProvider(List<Contact> contact) {
        int ind=0;
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        for (int index = 0; index < contact.size(); index++) {
            Contact con = contact.get(index);
            int rawContactInsertIndex = ops.size();
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, con.getName()).withYieldAllowed(true).build());
            for(int i=0;i<con.getContactInfos().size();i++)
                if(con.getContactInfos().get(i)!=null)
                    if(con.getContactInfos().get(i).getEmailOrNumber()==5)
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, con.getContactInfos().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContactInfos().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
                    else
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, con.getContactInfos().get(i).getNumber()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, con.getContactInfos().get(i).getType()).withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
            ind++;
            Message message = new Message();
            message.what = index+100;
            handler.sendMessage(message);
            if(ind>=100||index==contact.size()-1){
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    ops.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ind=0;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
