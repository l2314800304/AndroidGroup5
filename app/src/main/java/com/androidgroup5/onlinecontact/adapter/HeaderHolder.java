package com.androidgroup5.onlinecontact.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.R;


public class HeaderHolder extends RecyclerView.ViewHolder {
    public final TextView tv_header;
    public HeaderHolder(View itemView) {
        super(itemView);
        tv_header = (TextView) itemView.findViewById(R.id.tv_header);
    }
}
