package com.androidgroup5.onlinecontact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncAddressBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_address_book);
        ((Button)findViewById(R.id.btn_sync)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContactFromLocal();
                if(((CheckBox)findViewById(R.id.ckb_local)).isChecked()){
                    if(UpdateLocal()){
                        Toast.makeText(SyncAddressBook.this,"通讯录同步成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SyncAddressBook.this,"通讯录同步失败，请重试！",Toast.LENGTH_SHORT).show();
                    }
                }
                if(((CheckBox)findViewById(R.id.ckb_cloud)).isChecked()){
                    if(UpdateCloud()){
                        Toast.makeText(SyncAddressBook.this,"通讯录同步成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SyncAddressBook.this,"通讯录同步失败，请重试！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean UpdateLocal(){
        return true;
    }
    private boolean UpdateCloud(){
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
                info.setData(phoneNumber);
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
                info.setData(emailAddress);
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
            info.setTime(date);
            info.setType(type);
            records.add(info);
        }
        recor.close();
        return records;
    }
}
