package com.androidgroup5.onlinecontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SyncAddressBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_address_book);
        ((Button)findViewById(R.id.btn_sync)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)findViewById(R.id.ckb_local)).isChecked()){
                    if(UpdateLocal()){
                        Toast.makeText(SyncAddressBook.this,"本地通讯录更新成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SyncAddressBook.this,"本地通讯录更新失败，请重试！",Toast.LENGTH_SHORT).show();
                    }
                }
                if(((CheckBox)findViewById(R.id.ckb_cloud)).isChecked()){
                    if(UpdateCloud()){
                        Toast.makeText(SyncAddressBook.this,"云端通讯录更新成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SyncAddressBook.this,"云端通讯录更新失败，请重试！",Toast.LENGTH_SHORT).show();
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
}
