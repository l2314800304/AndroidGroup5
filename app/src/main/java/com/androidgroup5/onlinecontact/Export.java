package com.androidgroup5.onlinecontact;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;
import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.adapter.ContactAdapter;
import com.androidgroup5.onlinecontact.cn.CNPinyin;
import com.androidgroup5.onlinecontact.cn.CNPinyinFactory;
import com.androidgroup5.onlinecontact.search.CharIndexView;
import com.androidgroup5.onlinecontact.search.sContact;
import com.androidgroup5.onlinecontact.stickyheader.StickyHeaderDecoration;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import android.database.Cursor;
import android.net.Uri;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Export extends AppCompatActivity {
    private Button btnExported;
    private RecyclerView rv_main;
    private ContactAdapter adapter;
    User u;
    private ArrayList<CNPinyin<sContact>> contactList = new ArrayList<>();
    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        u=((UserParameter) getApplication()).getLocal();
        rv_main = (RecyclerView) findViewById(R.id.rv_main);

        btnExported=(Button)findViewById(R.id.btnExported) ;
        btnExported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Export.this, "导出成功", Toast.LENGTH_LONG).show();
                export();
            }
        });
        ((Button)findViewById(R.id.iv_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Export.this, Find.class));
            }
        });
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);



        adapter = new ContactAdapter(contactList);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));

        getPinyinList();
    }

    private void getPinyinList() {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<sContact>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<sContact>>> subscriber) {

                if (!subscriber.isUnsubscribed()) {
                    List<sContact> contactLists = new ArrayList<>();
                    Random random = new Random(System.currentTimeMillis());
                    int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
                    for (int i = 0; i < u.getContact().size(); i++) {
                        int urlIndex = random.nextInt(URLS.length - 1);
                        int url = URLS[urlIndex];
                        contactLists.add(new sContact(u.getContact().get(i).getName(),u.getContact().get(i).getContactInfos().get(0).getNumber(), url));
                    }
                    List<CNPinyin<sContact>> contactList = CNPinyinFactory.createCNPinyinList(contactLists);
                    Collections.sort(contactList);
                    subscriber.onNext(contactList);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CNPinyin<sContact>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CNPinyin<sContact>> cnPinyins) {
                        contactList.addAll(cnPinyins);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private List<Contact> GetContactFromLocal() {
        List<Contact> contacts = new ArrayList<Contact>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentResolver contentResolver = Export.this.getContentResolver();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void export() {
        try{
            Uri uri1 = ContactsContract.Contacts.CONTENT_URI;
            ContentResolver contentResolver = Export.this.getContentResolver();
            Cursor cur = contentResolver.query(uri1, null, null, null, null);
            int index = cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
            Date d=new Date();

                File fs = new File(Environment.getExternalStorageDirectory().toString()+"/"+d.getYear()+""+d.getMonth()+""+d.getDay()+""+d.getHours()+""+d.getMinutes()+""+d.getSeconds()+".vcf");


            FileOutputStream fout = new FileOutputStream(fs);
            byte[] data = new byte[1024 * 1];
			while(cur.moveToNext()) {
                String lookupKey = cur.getString(index);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                AssetFileDescriptor fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fin = fd.createInputStream();
                int len = -1;
                while ((len = fin.read(data)) != -1) {
                    fout.write(data, 0, len);
                }
                fin.close();
            }
            fout.close();
		}catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}



