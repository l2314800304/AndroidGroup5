package com.androidgroup5.onlinecontact;
import android.app.Activity;
import android.os.Bundle;
import com.androidgroup5.onlinecontact.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
public class MySixActivity extends Activity {
    private MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView =  findViewById(R.id.id_bmapView);//获取地图组件
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


