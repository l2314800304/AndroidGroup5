package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;

import java.util.ArrayList;
import java.util.List;

public class Insert extends AppCompatActivity {
    private TextView contactName;
    private TextView contactEmail;
    private TextView contactMobilePhone;
    private TextView contactTelPhone;
    private TextView contactCorpPhone;
    private TextView contactCorpEmail;
    private TextView contactOtherInfo;
    private Button insert;
    private Button clear;

    //EmailOrNumber 0,Email;1,电话


    private String name = contactName.getText().toString().trim();
    private String email = contactEmail.getText().toString().trim();
    private String mobilePhone = contactMobilePhone.getText().toString().trim();
    private String telPhone = contactTelPhone.getText().toString().trim();
    private String corpPhone = contactCorpPhone.getText().toString().trim();
    private String corpEmail = contactCorpEmail.getText().toString().trim();

    private List<ContactInfos> list = new ArrayList();

    private void init() {
        contactName = (TextView)findViewById(R.id.contactName);
        contactEmail = (TextView)findViewById(R.id.contactEmail);
        contactMobilePhone = (TextView)findViewById(R.id.contactMobilePhone);
        contactTelPhone = (TextView)findViewById(R.id.contactTelPhone);
        contactCorpPhone = (TextView)findViewById(R.id.contactCorpPhone);
        contactCorpEmail = (TextView)findViewById(R.id.contactCorpEmail);
        contactOtherInfo = (TextView)findViewById(R.id.otherInfo);
        insert = (Button)findViewById(R.id.btn_insert);
        clear = (Button)findViewById(R.id.btn_clear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactName.setText("");
                contactEmail.setText("");
                contactMobilePhone.setText("");
                contactTelPhone.setText("");
                contactCorpPhone.setText("");
                contactCorpEmail.setText("");
                contactOtherInfo.setText("");
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    private void insert() {
        judge();
        //
        //



    }

    private void judge() {
        if(!email.isEmpty()) {
            ContactInfos  ci = new ContactInfos();
            ci.setEmailOrNumber(0);
            ci.setType("私人邮箱");
            ci.setNumber(email);
            list.add(ci);
        }

        if(!mobilePhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(1);
            ci.setType("移动电话");
            ci.setNumber(mobilePhone);
            list.add(ci);
        }

        if(!telPhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(1);
            ci.setType("私人座机");
            ci.setNumber(telPhone);
            list.add(ci);
        }

        if(!corpEmail.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(1);
            ci.setType("公司邮箱");
            ci.setNumber(corpEmail);
        }

        if(!corpPhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(1);
            ci.setType("公司电话");
            ci.setNumber(corpPhone);
        }
    }
}
