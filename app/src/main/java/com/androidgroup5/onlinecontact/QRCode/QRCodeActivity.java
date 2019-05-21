package com.androidgroup5.onlinecontact.QRCode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.androidgroup5.onlinecontact.R;
import java.io.File;
public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        //内容
        final EditText contentET = (EditText) findViewById(R.id.create_qr_content);
        //显示二维码图片
        final ImageView imageView = (ImageView) findViewById(R.id.create_qr_iv);
        //是否添加Logo
//        final CheckBox addLogoCB = (CheckBox) findViewById(R.id.create_qr_addLogo);
        Button createQrBtn = (Button) findViewById(R.id.create_qr_btn);
        createQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filePath = getFileRoot(QRCodeActivity.this) + File.separator
                        + "qr_" + System.currentTimeMillis() + ".jpg";
                //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean success = QRCodeUtil.createQRImage(contentET.getText().toString().trim(), 800, 800,
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
        });

//        Button saveimageBtn = (Button)findViewById(R.id.save_image_btn);
//        saveimageBtn.setOnClickListener(new View.OnClickListener() {
//                                            public void onClick(View v) {
//                                                Bitmap obmp = Bitmap.createBitmap(imageView.getDrawingCache());
//                                                boolean bol = ImgUtils.saveImageToGallery(null, obmp);
//                                                if (bol){
//                                                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
//                                                }
//                                                else{
//                                                    Toast.makeText(getApplicationContext(),"保存失败",Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });

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
