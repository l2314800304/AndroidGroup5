package com.androidgroup5.onlinecontact;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class Phone extends Activity {

    private TextView tv_text1;
    private TextView tv_text2;
    private Button bt_delete;
    private Button bt_zero;
    private Button bt_one;
    private Button bt_two;
    private Button bt_three;
    private Button bt_four;
    private Button bt_five;
    private Button bt_six;
    private Button bt_seven;
    private Button bt_eight;
    private Button bt_nine;
    private Button bt_star;
    private Button bt_bottom;
    private Button bt_call;


    private TextView tempText;
    private String all = "";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    startActivity(new Intent().setClass(Phone.this,Find.class));
                    return true;
                case R.id.navigation_record:
                    startActivity(new Intent().setClass(Phone.this,CallLogActivity.class));
                    return true;
                case R.id.navigation_sync:
                    startActivity(new Intent().setClass(Phone.this,SyncAddressBook.class));
                    return true;
                case R.id.navigation_call:
                    return true;
                case R.id.navigation_mine:
                    startActivity(new Intent().setClass(Phone.this,SkipActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        bt_zero = (Button) findViewById(R.id.bt_zero);
        bt_one = (Button) findViewById(R.id.bt_one);
        bt_two = (Button) findViewById(R.id.bt_two);
        bt_three = (Button) findViewById(R.id.bt_three);
        bt_four = (Button) findViewById(R.id.bt_four);
        bt_five = (Button) findViewById(R.id.bt_five);
        bt_six = (Button) findViewById(R.id.bt_six);
        bt_seven = (Button) findViewById(R.id.bt_seven);
        bt_eight = (Button) findViewById(R.id.bt_eight);
        bt_nine = (Button) findViewById(R.id.bt_nine);
        bt_star = (Button) findViewById(R.id.bt_star);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);
        bt_call = (Button) findViewById(R.id.bt_call);


        bt_zero.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_zero);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_one.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_one);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_two.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_two);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_three.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_three);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_four.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_four);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_five.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_five);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_six.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_six);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_seven.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_seven);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_eight.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_eight);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_nine.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_nine);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_star.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_star);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_bottom.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.bt_bottom);
                all += tempText.getText().toString();
                tv_text1.setText(all);
                tv_text2.setText(all);
                bt_delete.setEnabled(true);
            }
        });

        bt_call.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.tv_text2);
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + tempText.getText().toString().trim()));
                startActivity(intent);
            }
        });

        bt_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                tempText = (TextView) findViewById(R.id.tv_text2);
                 if(tempText.getText().toString().length()>0)
                  {
                    all = all.substring(0, all.length() - 1);
                     tv_text1.setText(all);
                     tv_text2.setText(all);
                  }
                else
                {
                    bt_delete.setEnabled(false);
                    //bt_delete.setEnabled(true);
                }

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(3).getItemId());

    }
}