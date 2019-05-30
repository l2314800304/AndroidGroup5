package com.androidgroup5.onlinecontact;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.google.gson.Gson;
import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class EditContactDetailActivity extends AppCompatActivity {
    Button btn_backToDetail, btn_update;
    EditText et_contact_number, et_contact_email, et_contact_type;
    User user=null;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    Toast.makeText(EditContactDetailActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    //添加跳转页面
                    backToDetail();
                    break;
                case 11:
                    Toast.makeText(EditContactDetailActivity.this,"网络连接出现异常，修改失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_detail);
        int index = this.getIntent().getExtras().getInt("index");
        user=((UserParameter)getApplication()).getLocal();
        Contact con = user.getContact().get(index);
        String contact_name = con.getName(),
                contact_birthday = con.getBirthday(),
                contact_id = String.valueOf(con.getID());
        List<ContactInfos> info=con.getContactInfos();
        ContactInfos cin = info.get(0);
        String contact_number = cin.getNumber(),
                contact_type = cin.getType();
        init(contact_name, contact_birthday,contact_number,contact_type,contact_id);
    }

    public void init(String contact_name, String contact_birthday, String contact_number, String contact_type, final String contact_id) {
        btn_backToDetail = (Button) findViewById(R.id.btn_backToDetail);
        btn_update = (Button) findViewById(R.id.btn_update);
        final EditText et_contact_name =(EditText)findViewById(R.id.et_contact_name);
        et_contact_name.setText(contact_name);
        et_contact_number = (EditText) findViewById(R.id.et_contact_number);
        et_contact_number.setText(contact_number);
        final EditText et_contact_birthday =(EditText)findViewById(R.id.et_contact_birthday);
        et_contact_birthday.setText(contact_birthday);
        EditText et_contact_type =(EditText)findViewById(R.id.et_contact_type);
        et_contact_type.setText(contact_type);
        btn_backToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDetail();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(EditContactDetailActivity.this, "修改中...", Toast.LENGTH_LONG).show();
                    String  id = contact_id,
                            contact_name = et_contact_name.getText().toString(),
                            contact_number = et_contact_number.getText().toString(),
                            birthday=et_contact_birthday.getText().toString();
                    UpadteContact(id,contact_name,birthday,contact_number);
                } else {
                    Toast.makeText(EditContactDetailActivity.this, "修改失败，请检查所填写的信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void UpadteContact(String ID,String Name,String Birthday,String Number){
        HashMap<String, String> paramsMap = new HashMap<>();
        Contact con=new Contact();
        con.setName(Name);
        con.setID(Integer.parseInt(ID));
        con.setBirthday(Birthday);
        List<ContactInfos> info=new ArrayList<>();
        ContactInfos c=new ContactInfos();
        c.setEmailOrNumber(5);
        c.setID(0);
        c.setNumber(Number);
        c.setType("2");
        info.add(c);
        con.setContactInfos(info);
        paramsMap.put("Contact_ID", ID);
        paramsMap.put("Contact", (new Gson().toJson(con)));
        paramsMap.put("Birthday", Birthday);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS).build();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/UpdateContact.ashx")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if(response.body().string().equals("OK")){
                        Message message = new Message();
                        message.what = 10;
                        handler.sendMessage(message);
                    }else{
                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);
                    }
                } else {
                    Message message = new Message();
                    message.what = 11;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void backToDetail() {
        // startActivity(new Intent().setClass(ContactDetailActivity.this));
    }

    private boolean checkData() {
        String number = et_contact_number.getText().toString();
        if (number.length() != 11) {
            return false;
        } else {
            return true;
        }
    }


}
