package com.androidgroup5.onlinecontact;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Export extends AppCompatActivity {
    private Button btnExported;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        btnExported=(Button)findViewById(R.id.btnExported) ;
        btnExported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Export.this, "导出成功", Toast.LENGTH_LONG).show();
            }
        });
    }


}
