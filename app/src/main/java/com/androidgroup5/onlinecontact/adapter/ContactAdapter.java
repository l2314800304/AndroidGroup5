package com.androidgroup5.onlinecontact.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.androidgroup5.onlinecontact.R;
import com.androidgroup5.onlinecontact.cn.CNPinyin;
import com.androidgroup5.onlinecontact.search.sContact;
import com.androidgroup5.onlinecontact.stickyheader.StickyHeaderAdapter;



public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private final List<CNPinyin<sContact>> cnPinyinList;
    private Activity activity;
    public ContactAdapter(List<CNPinyin<sContact>> cnPinyinList,Activity ac) {
        activity=ac;
        this.cnPinyinList = cnPinyinList;
    }

    @Override
    public int getItemCount() {
        return cnPinyinList.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false),activity);
    }


    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        sContact contact = cnPinyinList.get(position).data;
        holder.iv_header.setImageResource(contact.imgUrl);
        holder.tv_name.setText(contact.name);
    }

    @Override
    public long getHeaderId(int childAdapterPosition) {
        return cnPinyinList.get(childAdapterPosition).getFirstChar();
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int childAdapterPosition) {
        holder.tv_header.setText(String.valueOf(cnPinyinList.get(childAdapterPosition).getFirstChar()));
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false));
    }

}
