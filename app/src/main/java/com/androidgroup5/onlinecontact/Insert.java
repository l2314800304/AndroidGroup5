package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Insert extends AppCompatActivity {
    private TextView contactName;
    private TextView contactEmail;
    private TextView contactMobilePhone;
    private TextView contactTelPhone;
    private TextView contactCorpPhone;
    private TextView contactPosition;
    private TextView contactOtherInfo;

    private void init() {
        //contactName = (TextView)findViewById()
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
    }
}
