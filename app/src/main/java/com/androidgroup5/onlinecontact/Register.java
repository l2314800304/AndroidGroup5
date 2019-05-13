package com.androidgroup5.onlinecontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    Button btn_backToLogin,btn_register;
    EditText et_user,et_pass,et_pass1,et_remark;
    RadioButton rbtn_man;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    private void init(){
        btn_backToLogin=(Button) findViewById(R.id.btn_backToLogin);
        btn_register=(Button) findViewById(R.id.btn_register);
        et_user=(EditText) findViewById(R.id.et_user);
        et_pass=(EditText) findViewById(R.id.et_pass);
        et_pass1=(EditText) findViewById(R.id.et_pass1);
        et_remark=(EditText) findViewById(R.id.et_remark);
        btn_backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    if(register()){
                        Toast.makeText(Register.this,"注册成功，返回登录页面...",Toast.LENGTH_LONG).show();
                        backToLogin();
                    }
                }else{
                    Toast.makeText(Register.this,"注册失败，请检查注册信息！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean register(){
        String user=et_user.getText().toString(),
                pass=et_pass.getText().toString(),
                sex=rbtn_man.isChecked()?"男":"女",
                remark=et_remark.getText().toString();
        //进行注册操作
        return true;
    }
    private void backToLogin(){

    }
    private boolean checkData(){
        String user=et_user.getText().toString(),
                pass=et_pass.getText().toString(),
                pass1=et_pass1.getText().toString(),
                remark=et_remark.getText().toString();
        if(user.length()<4||user.length()>8||pass.length()<6||pass.length()>16||!pass.equals(pass1)||remark.length()>50){
            return false;
        }else{
            return true;
        }
    }
}
