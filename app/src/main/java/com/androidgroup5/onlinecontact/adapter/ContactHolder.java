package com.androidgroup5.onlinecontact.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.R;


public class ContactHolder extends RecyclerView.ViewHolder {

    public final ImageView iv_header;

    public final TextView tv_name;

    public ContactHolder(View itemView) {
        super(itemView);
        iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
    }
}
