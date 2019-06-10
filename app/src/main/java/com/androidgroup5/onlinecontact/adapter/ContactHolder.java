package com.androidgroup5.onlinecontact.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.ContactDetailActivity;
import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.androidgroup5.onlinecontact.R;
import com.androidgroup5.onlinecontact.UserParameter;

import java.util.List;


public class ContactHolder extends RecyclerView.ViewHolder {

    public final ImageView iv_header;
    public final TextView tv_name;
    public final TextView tv_phone;
    private Activity activity;
    public ContactHolder(View itemView, Activity ac) {
        super(itemView);
        activity=ac;
        iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Contact> contactList=((UserParameter)activity.getApplication()).getLocal().getContact();
                int index=0,tmp=getLayoutPosition();
                for(int i=0;i<contactList.size();i++)
                    if(contactList.get(i).getName().equals(tv_name.getText().toString().split(" ")[0]))
                        index=i;
                ((UserParameter)activity.getApplication()).setIndex(index);
                activity.startActivity(new Intent().setClass(activity.getApplicationContext(),ContactDetailActivity.class));
            }
        });
    }
}
