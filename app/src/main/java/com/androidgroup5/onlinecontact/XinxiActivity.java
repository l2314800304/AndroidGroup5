package com.androidgroup5.onlinecontact;
import java.util.Calendar;
        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.TextView;

/**
 * 时间选择器
 */
public class XinxiActivity extends Activity {
    Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinxi_layout);
        Button btn8=findViewById(R.id.btntuichu);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it8=new Intent();
                it8.setClass(XinxiActivity.this,SkipActivity.class);
                XinxiActivity.this.startActivity(it8);
            }
        });

    }



}
