package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Insert extends AppCompatActivity {
    private TextView contactName;
    private TextView contactEmail;
    private TextView contactMobilePhone;
    private TextView contactTelPhone;
    private TextView contactCorpPhone;
    private TextView contactPosition;
    private TextView contactOtherInfo;
    private Button insert;
    private Button clear;

    private void init() {
        contactName = (TextView)findViewById(R.id.contactName);
        contactEmail = (TextView)findViewById(R.id.contactEmail);
        contactMobilePhone = (TextView)findViewById(R.id.contactMobilePhone);
        contactTelPhone = (TextView)findViewById(R.id.contactCorpPhone);
        contactPosition = (TextView)findViewById(R.id.Position);
        contactOtherInfo = (TextView)findViewById(R.id.otherInfo);
        insert = (Button)findViewById(R.id.btn_insert);
        clear = (Button)findViewById(R.id.btn_clear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();
        //clear.setOnClickListener((v) -> {

        //});
    }
}
