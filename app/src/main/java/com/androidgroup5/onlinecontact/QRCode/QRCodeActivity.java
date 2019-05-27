package com.androidgroup5.onlinecontact.QRCode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.androidgroup5.onlinecontact.R;

import java.io.File;

public class QRCodeActivity extends AppCompatActivity {
    String ContactName = "张三",
            ContactNumber ="31321231";
//                    this.getIntent().getExtras().getString("ContactNumber");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        //内容
        final TextView tv_contactname = (TextView) findViewById(R.id.tv_contactname);
        tv_contactname.setText(ContactName);
        //显示二维码图片
        final ImageView imageView = (ImageView) findViewById(R.id.create_qr_iv);
        final String filePath = getFileRoot(QRCodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(ContactNumber.trim(), 800, 800,
                        true ? null : null,
                        filePath);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();


    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }
}
