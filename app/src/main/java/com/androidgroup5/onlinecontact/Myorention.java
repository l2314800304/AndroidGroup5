package com.androidgroup5.onlinecontact;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

public class Myorention extends AppCompatActivity {
    private MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorention);
        mMapView = (MapView) findViewById(R.id.id_bmapView);//获取地图组件
        Button btn9=findViewById(R.id.dwtc);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it9=new Intent();
                it9.setClass(Myorention.this,SkipActivity.class);
                Myorention.this.startActivity(it9);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
