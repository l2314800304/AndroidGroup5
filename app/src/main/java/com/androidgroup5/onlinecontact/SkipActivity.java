package com.androidgroup5.onlinecontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2019/5/26.
 */

public class SkipActivity extends Activity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    startActivity(new Intent().setClass(SkipActivity.this,Find.class));
                    return true;
                case R.id.navigation_record:
                    startActivity(new Intent().setClass(SkipActivity.this,CallLogActivity.class));
                    return true;
                case R.id.navigation_sync:
                    startActivity(new Intent().setClass(SkipActivity.this,SyncAddressBook.class));
                    return true;
                case R.id.navigation_call:
                    startActivity(new Intent().setClass(SkipActivity.this,Phone.class));
                    return true;
                case R.id.navigation_mine:
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_layout);
        Button btn=findViewById(R.id.btn7);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent();
                it1.setClass(SkipActivity.this,XinxiActivity.class);
                SkipActivity.this.startActivity(it1);

            }
        });
        Button btn3=findViewById(R.id.btn6);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it3=new Intent();
                it3.setClass(SkipActivity.this,Remark.class);
                SkipActivity.this.startActivity(it3);

            }
        });
        Button bt=findViewById(R.id.btn3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2=new Intent();
                it2.setClass(SkipActivity.this,Repassword.class);
                SkipActivity.this.startActivity(it2);

            }
        });
        Button btn5=findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it5=new Intent();
                it5.setClass(SkipActivity.this,Login.class);
                SkipActivity.this.startActivity(it5);

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(4).getItemId());
        ((Button)findViewById(R.id.btn_sign)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(SkipActivity.this,SignDate.class));
            }
        });
        ((Button)findViewById(R.id.btn_birthday)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(SkipActivity.this,Birthday.class));
            }
        });
    }

}
