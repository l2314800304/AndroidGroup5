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
        setContentView(R.layout.activity_skip);
        Button btn=findViewById(R.id.btn3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it1=new Intent();
                it1.setClass(SkipActivity.this,XinxiActivity.class);
                SkipActivity.this.startActivity(it1);

            }
        });

    }

}

