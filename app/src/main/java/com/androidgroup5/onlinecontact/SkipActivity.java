package com.androidgroup5.onlinecontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2019/5/26.
 */

public class SkipActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_layout);
        Button btn=findViewById(R.id.btn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent();
                it1.setClass(SkipActivity.this,XinxiActivity.class);
                SkipActivity.this.startActivity(it1);

            }
        });
        Button bt=findViewById(R.id.btn4);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2=new Intent();
                it2.setClass(SkipActivity.this,Updatemm.class);
                SkipActivity.this.startActivity(it2);

            }
        });
    }

}
