package com.androidgroup5.onlinecontact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidgroup5.onlinecontact.QRCode.QRCodeActivity;

public class MainActivity extends AppCompatActivity {
    Button btn01,btn02,
            btn11,btn12,
            btn21,btn22,
            btn31,btn32,
            btn41,btn42,
            btn51,btn52,
            btn61,btn62,
            btn71,btn72,
            btn81,btn82,
            btn91,btn92;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void init(){
        btn01=(Button)findViewById(R.id.btn_01);
        btn01.setOnClickListener(new mClick(1));
        btn02=(Button)findViewById(R.id.btn_02);
        btn02.setOnClickListener(new mClick(2));
        btn11=(Button)findViewById(R.id.btn_11);
        btn11.setOnClickListener(new mClick(11));
        btn12=(Button)findViewById(R.id.btn_12);
        btn12.setOnClickListener(new mClick(12));
        btn21=(Button)findViewById(R.id.btn_21);
        btn21.setOnClickListener(new mClick(21));
        btn22=(Button)findViewById(R.id.btn_22);
        btn22.setOnClickListener(new mClick(22));
        btn31=(Button)findViewById(R.id.btn_31);
        btn31.setOnClickListener(new mClick(31));
        btn32=(Button)findViewById(R.id.btn_32);
        btn32.setOnClickListener(new mClick(32));
        btn41=(Button)findViewById(R.id.btn_41);
        btn41.setOnClickListener(new mClick(41));
        btn42=(Button)findViewById(R.id.btn_42);
        btn42.setOnClickListener(new mClick(42));
        btn51=(Button)findViewById(R.id.btn_51);
        btn51.setOnClickListener(new mClick(51));
        btn52=(Button)findViewById(R.id.btn_52);
        btn52.setOnClickListener(new mClick(52));
        btn61=(Button)findViewById(R.id.btn_61);
        btn61.setOnClickListener(new mClick(61));
        btn62=(Button)findViewById(R.id.btn_62);
        btn62.setOnClickListener(new mClick(62));
        btn71=(Button)findViewById(R.id.btn_71);
        btn71.setOnClickListener(new mClick(71));
        btn72=(Button)findViewById(R.id.btn_72);
        btn72.setOnClickListener(new mClick(72));
        btn81=(Button)findViewById(R.id.btn_81);
        btn81.setOnClickListener(new mClick(81));
        btn82=(Button)findViewById(R.id.btn_82);
        btn82.setOnClickListener(new mClick(82));
        btn91=(Button)findViewById(R.id.btn_91);
        btn91.setOnClickListener(new mClick(91));
        btn92=(Button)findViewById(R.id.btn_92);
        btn92.setOnClickListener(new mClick(92));
    }
    class mClick implements View.OnClickListener{
        int _id;
        public mClick(int id){
            _id=id;
        }

        @Override
        public void onClick(View v) {
            switch (_id){
                case 1:
                    startActivity(new Intent().setClass(MainActivity.this,Register.class));
                    break;
                case 2:
                    startActivity(new Intent().setClass(MainActivity.this,SyncAddressBook.class).putExtra("UserName","宋甜乐"));
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 21:
                    startActivity(new Intent().setClass(MainActivity.this,Delete.class));
                    break;
                case 22:
                    startActivity(new Intent().setClass(MainActivity.this,Remark.class));
                    break;
                case 31:
                    startActivity(new Intent().setClass(MainActivity.this,Login.class));
                    break;
                case 32:
                    startActivity(new Intent().setClass(MainActivity.this, ShowContactActivity.class));
                    break;
                case 41://通讯录详情页
                    startActivity(new Intent().setClass(MainActivity.this, ContactDetailActivity.class));
                    break;
                case 42://通话记录
                    startActivity(new Intent().setClass(MainActivity.this, CallLogActivity.class));
                    break;
                case 51:
                    startActivity(new Intent().setClass(MainActivity.this,Insert.class));
                    break;
                case 52:
                    break;
                case 61:
                    break;
                case 62:
                    break;
                case 71:
                    startActivity(new Intent().setClass(MainActivity.this,Phone.class));
                    break;
                case 72:
                    startActivity(new Intent().setClass(MainActivity.this,Birthday.class));
                    break;
                case 81:
                    startActivity(new Intent().setClass(MainActivity.this,QRCodeActivity.class));
                    break;
                case 82:
                    startActivity(new Intent().setClass(MainActivity.this,EditContactDetailActivity.class));
                    break;
                case 91:
                    startActivity(new Intent().setClass(MainActivity.this,Find.class));
                    break;
                case 92:
                    startActivity(new Intent().setClass(MainActivity.this,Export.class));
                    break;
            }
        }
    }
}
